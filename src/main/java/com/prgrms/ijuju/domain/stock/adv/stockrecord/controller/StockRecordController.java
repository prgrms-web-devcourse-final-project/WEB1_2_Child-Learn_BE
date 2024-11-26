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
        // DTO에서 memberId를 받아 Member 객체를 조회
        Long memberId = requestDto.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다: " + memberId));

        // 서비스로 RequestDto와 Member 객체 전달
        StockRecord record = stockRecordService.saveRecord(requestDto, member);
        return ResponseEntity.ok(record);
    }

    // 특정 AdvancedInvest의 거래 내역 조회
    @GetMapping("/{advId}")
    public ResponseEntity<List<StockRecordResponseDto>> getRecordsByAdvId(@PathVariable Long advId) {
        List<StockRecordResponseDto> response = stockRecordService.getRecordsByAdvId(advId).stream()
                .map(StockRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 특정 주식의 보유량 조회
    @GetMapping("/{advId}/{stockSymbol}/quantity")
    public ResponseEntity<Integer> getOwnedStock(@PathVariable Long advId, @PathVariable String stockSymbol) {
        int quantity = stockRecordService.calculateOwnedStock(advId, stockSymbol);
        return ResponseEntity.ok(quantity);
    }

    // 모든 주식의 보유량 조회
    @GetMapping("/{advId}/all-quantities")
    public ResponseEntity<Map<String, Integer>> getAllOwnedStocks(@PathVariable Long advId) {
        Map<String, Integer> ownedStocks = stockRecordService.calculateAllOwnedStocks(advId);
        return ResponseEntity.ok(ownedStocks);
    }
}

