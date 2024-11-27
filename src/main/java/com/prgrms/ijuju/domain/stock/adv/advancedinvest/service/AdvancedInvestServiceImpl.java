package com.prgrms.ijuju.domain.stock.adv.advancedinvest.service;

import com.prgrms.ijuju.common.util.WebSocketUtil;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.entity.StockStatus;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.service.PointService;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.repository.AdvancedInvestRepository;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request.StockRecordRequestDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
public class AdvancedInvestServiceImpl implements AdvancedInvestService {

    private final AdvancedInvestRepository advancedInvestRepository;
    private final AdvStockRepository advStockRepository;
    private final MemberRepository memberRepository;
    private final StockRecordService stockRecordService;
    private final PointService pointService;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    // activeGame 은 이제 AdvancedInvest Id(gameId) 와 activeTimer 를 매핑하는 HashMap 입니다.
    // 그냥 간단하게 타이머랑 게암 Id 를 합쳐서 보관한다고 생각하면 편안합니다.
    private final Map<Long, ScheduledFuture<?>> activeGames = new ConcurrentHashMap<>();

    private final Map<Long, Integer> countDown = new ConcurrentHashMap<>();

    private final Map<Long, WebSocketSession> gameSessions = new ConcurrentHashMap<>();


    //게임 타이머. 게임은 총 7분 진행되며, 1분은 장전 거래 시간, 5분은 거래 시간, 마지막 1분은 장후 거래 시간
    //SechduledExecutorService 를 사용하였습니다.   >>>  https://lslagi.tistory.com/entry/JAVA-ScheduledExecutorService-Timer-%EC%A0%81%EC%9A%A9%EC%9E%90%EB%8F%99-%EB%A6%AC%ED%94%8C%EB%A0%88%EC%89%AC
    @Override
    public void startGameTimer(WebSocketSession session, Long gameId, int startSecond) {
        Runnable gameTask = new Runnable() {
            int second = startSecond;

            public void run() {
                try {
                    countDown.put(gameId, second);

                    if (second == 60) { // 장전 시간 종료 시점
                        WebSocketUtil.send(session, "장전 거래 시간이 종료되었습니다. 장이 열렸습니다.");
                    } else if (second == 360) { // 거래 시간 종료 시점
                        WebSocketUtil.send(session, "장이 닫혔습니다. 장후 거래 시간으로 거래 마무리를 해주세요");
                    }

                    if (second < 60) { // 장전 거래 시간 1분 > ReferenceData
                        sendReferenceData(session);
                    } else if (second <= 360) { // 거래 시간 5분 > LiveData >> 총 6개의 데이터가 전돨되어야 한다.
                        int livePhase = (second - 60) / 60;
                        sendLiveData(session, livePhase);
                    } else if (second < 420) { // 장후 거래 시간 1분 > Data 없음
                        sendEndSignal(session);
                    } else {
                        endGame(gameId); // 게임 종료

                        // activeGame.remove(gameId) 시 남는것은 activeTimer (activeGame 은 {id, activeTimer} 이기 때문)
                        // 즉 현재 진행중인 activeTimer 를 저장하는 과정.
                        ScheduledFuture<?> activeTimer = activeGames.remove(gameId);
                        if (activeTimer != null) {
                            activeTimer.cancel(false); //타이머 정지
                        }
                        countDown.remove(gameId);
                    }

                    second++; // 다음 초로 진행

                } catch (Exception e) {
                    e.printStackTrace();
                    pauseGame(gameId, second); // 예외 발생 시 현재 초로 게임 일시 정지
                }
            }
        };

        //activeTimer 는 카운터 작업을 관리하는 객체
        ScheduledFuture<?> activeTimer = executorService.scheduleAtFixedRate(gameTask, 0, 1, TimeUnit.SECONDS);
        activeGames.put(gameId, activeTimer);
        countDown.put(gameId, startSecond);
        gameSessions.put(gameId, session);
    }

    // Reference Data
    private void sendReferenceData(WebSocketSession session) {
        List<AdvStock> referenceData = advStockRepository.findByDataType(DataType.REFERENCE);
        WebSocketUtil.send(session, referenceData);
    }

    // Live Data
    private void sendLiveData(WebSocketSession session, int hour) {
        List<AdvStock> liveData = advStockRepository.findByDataType(DataType.LIVE);
        if (hour < liveData.size()) {
            AdvStock dataForHour = liveData.get(hour);
            WebSocketUtil.send(session, dataForHour);
        }
    }

    // 종료 메시지. 프론트 측은 이걸 기준으로 게임 종료 문구를 올려주면 된다
    private void sendEndSignal(WebSocketSession session) {
        WebSocketUtil.send(session, "게임 종료");
    }


    //게임 실행 메소드로, 새벽 6시부터 8시 사이에는 게임이 제한되며, 오늘 하루동안 게임을 이미 했다면 또한 제한된다
    @Transactional
    @Override
    public void startGame(WebSocketSession session, Long memberId) {
        LocalTime now = LocalTime.now();
        LocalTime startRestrictedTime = LocalTime.of(6, 0); // 오전 6시
        LocalTime endRestrictedTime = LocalTime.of(8, 0);   // 오전 8시


        if (!now.isBefore(startRestrictedTime) && !now.isAfter(endRestrictedTime)) {
            throw new IllegalStateException("오전 6시부터 8시까지는 게임을 실행할 수 없습니다");
        }


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 찾지 못함 (memberId 조회 불가)"));

        // 오늘 이미 게임을 시작했는지 확인
        Optional<AdvancedInvest> existingInvest = advancedInvestRepository.findByMemberIdAndPlayedTodayTrue(memberId);
        if (existingInvest.isPresent()) {
            throw new IllegalStateException("게임이 오늘 이미 실행 되었습니다");
        }

        AdvancedInvest advancedInvest = advancedInvestRepository.save(
                AdvancedInvest.builder()
                        .member(member)
                        .startTime(System.currentTimeMillis())
                        .paused(false)
                        .build()
        );

        // 게임 타이머 시작
        startGameTimer(session, advancedInvest.getId(), 0);
    }


    // 게임 일시정지
    @Override
    @Transactional
    public void pauseGame(Long gameId, int second) {
        ScheduledFuture<?> activeTimer = activeGames.remove(gameId);
        if (activeTimer != null) {
            activeTimer.cancel(false);
        }

        AdvancedInvest advancedInvest = advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임 찾지 못함 (AdvancedInvestId 조회 불가)"));

        advancedInvest.setPaused(true); // 게임은 일시정지 상태로 표시
        advancedInvest.setCurrentSecond(second);
        advancedInvestRepository.save(advancedInvest);
    }

    // 게임 재개
    @Override
    @Transactional
    public void resumeGame(WebSocketSession session, Long gameId) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임 찾지 못함 (AdvancedInvest Id 조회 불가)"));
        if (!advancedInvest.isPaused()) {
            throw new IllegalStateException("게임이 이미 진행 중");
        }

        int currentSecond = advancedInvest.getCurrentSecond(); // 저장된 초 가져오기
        advancedInvest.setPaused(false); // 게임 상태를 진행 중으로 변경
        advancedInvestRepository.save(advancedInvest);

        startGameTimer(session, gameId, currentSecond); // 타이머 재개
    }

    // 게임 종료
    @Override
    @Transactional
    public void endGame(Long gameId) {
        activeGames.remove(gameId);

        AdvancedInvest advancedInvest = advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임 찾지 못함 (AdvancedInvest Id 조회 불가)"));

        advancedInvest.setPlayedToday(true);
        advancedInvestRepository.save(advancedInvest);

        WebSocketSession session = gameSessions.remove(gameId);
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 남은 시간 조회 메소드
    @Override
    public int getRemainingTime(Long gameId) {
        Integer currentSecond = countDown.get(gameId);
        if (currentSecond == null) {
            throw new IllegalArgumentException("게임이 진행 중이 아닙니다.");
        }
        return 420 - currentSecond; // 전체 시간에서 현재 초수 뺀 값 반환
    }


    //아침 7시 리셋
    @Override
    @Scheduled(cron = "0 0 7 * * ?") //
    @Transactional
    public void resetPlayedTodayStatus() {

        //7시에 모든 유저 PlayedToday = false;
        advancedInvestRepository.resetPlayedToday();

        // 진행 중인 게임 강제 종료
        for (Long gameId : activeGames.keySet()) {
            endGame(gameId);
        }

        // 정지 상태인 게임 강제 종료
        List<AdvancedInvest> pausedGames = advancedInvestRepository.findAllByPausedTrue();
        for (AdvancedInvest game : pausedGames) {
            endGame(game.getId());
        }
    }


    @Transactional
    @Override
    public void buyStock(Long gameId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임 찾지 못함 (AdvancedInvest Id 조회 불가)"));

        List<Double> closePrices = advStockRepository.findBySymbol(request.getStockSymbol())
                .map(AdvStock::getClosePrices)
                .orElseThrow(() -> new IllegalArgumentException("해당 주식이 존재하지 않음"));

        if (closePrices.isEmpty()) {
            throw new IllegalArgumentException("종가 데이터가 존재하지 않음");
        }

        Double latestClosePrice = closePrices.get(closePrices.size() - 1); // 가장 최신 종가

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("구매 수량은 0 보다 커야 한다");
        }

        // 주식 구매에 필요한 포인트 계산
        BigDecimal pointsRequired = BigDecimal.valueOf(latestClosePrice)
                .multiply(BigDecimal.valueOf(request.getQuantity()));
        // 포인트 차감 및 거래 기록
        PointRequestDTO pointRequest = PointRequestDTO.builder()
                .memberId(request.getMemberId())
                .pointAmount(pointsRequired.longValue())
                .build();

        pointService.simulateStockInvestment(pointRequest, StockType.valueOf(request.getStockSymbol()), StockStatus.BUY);

        StockRecordRequestDto recordRequest = StockRecordRequestDto.builder()
                .memberId(request.getMemberId())
                .stockSymbol(request.getStockSymbol())
                .tradeType(TradeType.BUY)
                .quantity(request.getQuantity())
                .price(pointsRequired)
                .advId(gameId)
                .build();

        // 거래 내역 저장
        stockRecordService.saveRecord(recordRequest, advancedInvest.getMember());
    }

    @Transactional
    @Override
    public void sellStock(Long gameId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임 찾지 못함 (AdvancedInvest Id 조회 불가)"));

        List<Double> closePrices = advStockRepository.findBySymbol(request.getStockSymbol())
                .map(AdvStock::getClosePrices)
                .orElseThrow(() -> new IllegalArgumentException("해당 주식을 찾을 수 없습니다."));

        if (closePrices.isEmpty()) {
            throw new IllegalArgumentException("해당 주식의 종가 데이터를 찾을 수 없습니다.");
        }

        Double latestClosePrice = closePrices.get(closePrices.size() - 1); // 가장 최신 종가

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("판매 수량은 0보다 커야 합니다.");
        }

        // 보유 주식 수량 확인
        int ownedQuantity = stockRecordService.calculateOwnedStock(gameId, request.getStockSymbol());
        if (ownedQuantity < request.getQuantity()) {
            throw new IllegalArgumentException("보유 수량보다 많은 주식을 판매할 수 없습니다.");
        }

        // 판매로 얻는 포인트 계산
        BigDecimal pointsEarned = BigDecimal.valueOf(latestClosePrice)
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // 포인트 환급 및 거래 기록
        PointRequestDTO pointRequest = PointRequestDTO.builder()
                .memberId(request.getMemberId())
                .pointAmount(pointsEarned.longValue())
                .build();

        pointService.simulateStockInvestment(pointRequest, StockType.valueOf(request.getStockSymbol()), StockStatus.SELL);

        StockRecordRequestDto recordRequest = StockRecordRequestDto.builder()
                .memberId(request.getMemberId())
                .stockSymbol(request.getStockSymbol())
                .tradeType(TradeType.SELL)
                .quantity(request.getQuantity())
                .price(pointsEarned)
                .advId(gameId)
                .build();

        // 거래 내역 저장
        stockRecordService.saveRecord(recordRequest, advancedInvest.getMember());
    }
}




    /* 혹시나 해서 만들어둔 서비스로, 해당 기능을 중복적으로 사용해서 만들어 뒀음. 근데 어차피 Exception 핸들링을 전부 리팩토링 할거라 지금은 폐기

    private AdvancedInvest getAdvancedInvestById(Long gameId) {
        return advancedInvestRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
    }
}

     */


    /* Http Request 용 startGame

    @Transactional
    @Override
    public AdvancedInvestResponseDto startGame(AdvancedInvestRequestDto request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));

        Optional<AdvancedInvest> existingInvest = advancedInvestRepository.findByMemberIdAndPlayedTodayTrue(request.getMemberId());
        if (existingInvest.isPresent()) {
            throw new IllegalStateException("오늘 이미 게임을 실행했습니다.");
        }

        AdvancedInvest advancedInvest = advancedInvestRepository.save(
                AdvancedInvest.builder()
                        .member(member)
                        .startTime(System.currentTimeMillis())
                        .paused(false)
                        .build()
        );
        return AdvancedInvestResponseDto.from(advancedInvest);
    }
     */


    /* Http Request 용 start, pause, end game

    @Transactional
    @Override
    public void pauseGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPaused(true);
    }

    @Transactional
    @Override
    public void resumeGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPaused(false);
    }

    @Transactional
    @Override
    public void endGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPlayedToday(false);
        advancedInvest.setPlayedToday(true);
        advancedInvestRepository.save(advancedInvest);
    }
     */



   /*
     * Http Request 용 Live / Reference Data 참조
    @Transactional(readOnly = true)
    @Override
    public StockResponseDto getLiveData(Long advId, String symbol, int hour) {
        List<AdvStock> stocks = advStockRepository.findBySymbolAndAdvIdOrderByTimestampAsc(symbol, advId);

        if (hour <= 0 || hour > stocks.size()) {
            throw new IllegalArgumentException("유효하지 않은 hour 값입니다.");
        }

        AdvStock stock = stocks.get(hour - 1);

        return StockResponseDto.fromEntity(stock);
    }


    @Transactional(readOnly = true)
    @Override
    public List<StockResponseDto> getReferenceData(Long advId) {
        return advStockRepository.findAllByDataType("REFERENCE")
                .stream()
                .map(StockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

     */
