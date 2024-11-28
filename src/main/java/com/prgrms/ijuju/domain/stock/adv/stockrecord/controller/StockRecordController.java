package com.prgrms.ijuju.domain.stock.adv.stockrecord.controller;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request.StockRecordRequestDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.response.StockRecordResponseDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stock-records")
@RequiredArgsConstructor
public class StockRecordController {

    private final StockRecordService stockRecordService;
    private final MemberRepository memberRepository;

    // 거래 내역 저장
    @PostMapping
    public ResponseEntity<StockRecord> saveRecord(@RequestBody StockRecordRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다: " + memberId));

        StockRecord record = stockRecordService.saveRecord(requestDto, member);
        return ResponseEntity.ok(record);
    }

    // 특정 AdvancedInvest의 거래 내역 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<StockRecordResponseDto>> getRecordsByAdvId(@PathVariable Long memberId) {
        List<StockRecordResponseDto> response = stockRecordService.getRecordsByAdvId(memberId).stream()
                .map(StockRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 특정 주식의 보유량 조회
    @GetMapping("/{memberId}/{stockSymbol}/quantity")
    public ResponseEntity<Double> getOwnedStock(@PathVariable Long memberId, @PathVariable String stockSymbol) {
        double quantity = stockRecordService.calculateOwnedStock(memberId, stockSymbol);
        return ResponseEntity.ok(quantity);
    }

    // 모든 주식의 보유량 조회
    @GetMapping("/{memberId}/all-quantities")
    public ResponseEntity<Map<String, Double>> getAllOwnedStocks(@PathVariable Long memberId) {
        Map<String, Double> ownedStocks = stockRecordService.calculateAllOwnedStocks(memberId);
        return ResponseEntity.ok(ownedStocks);
    }
}

