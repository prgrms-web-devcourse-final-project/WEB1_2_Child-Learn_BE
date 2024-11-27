package com.prgrms.ijuju.domain.stock.adv.stockrecord.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request.StockRecordRequestDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.repository.StockRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockRecordServiceImpl implements StockRecordService {

    private final StockRecordRepository stockRecordRepository;

    // 거래 내역 저장
    @Transactional
    public StockRecord saveRecord(StockRecordRequestDto requestDto, Member member) {
        // RequestDto를 사용하여 StockRecord 엔티티 생성
        StockRecord record = requestDto.toEntity(member);

        // 데이터베이스에 저장
        return stockRecordRepository.save(record);
    }

    // 특정 AdvancedInvest의 모든 거래 내역 조회
    @Transactional(readOnly = true)
    public List<StockRecord> getRecordsByAdvId(Long advId) {
        return stockRecordRepository.findByAdvancedInvest_Id(advId);
    }

    // 특정 주식의 거래 내역 조회
    @Transactional(readOnly = true)
    public List<StockRecord> getRecordsByStock(Long advId, String symbol) {
        return stockRecordRepository.findByAdvancedInvest_IdAndSymbol(advId, symbol);
    }

    // 보유 주식 계산
    @Transactional(readOnly = true)
    public int calculateOwnedStock(Long advId, String symbol) {
        List<StockRecord> records = stockRecordRepository.findByAdvancedInvest_IdAndSymbol(advId, symbol);

        int totalBought = records.stream()
                .filter(record -> record.getTradeType() == TradeType.BUY)
                .mapToInt(StockRecord::getQuantity)
                .sum();

        int totalSold = records.stream()
                .filter(record -> record.getTradeType() == TradeType.SELL)
                .mapToInt(StockRecord::getQuantity)
                .sum();

        return totalBought - totalSold; // 최종 보유 주식량 반환
    }

    // 모든 주식의 보유량 계산
    @Transactional(readOnly = true)
    public Map<String, Integer> calculateAllOwnedStocks(Long advId) {
        List<StockRecord> records = stockRecordRepository.findByAdvancedInvest_Id(advId);

        // 주식별 보유량 계산
        Map<String, Integer> ownedStocks = new HashMap<>();
        for (StockRecord record : records) {
            ownedStocks.putIfAbsent(record.getSymbol(), 0); // 초기값 설정
            int updatedQuantity = record.getTradeType() == TradeType.BUY
                    ? ownedStocks.get(record.getSymbol()) + record.getQuantity()
                    : ownedStocks.get(record.getSymbol()) - record.getQuantity();
            ownedStocks.put(record.getSymbol(), updatedQuantity);
        }

        return ownedStocks; // 주식 심볼별 최종 보유량 반환
    }
}