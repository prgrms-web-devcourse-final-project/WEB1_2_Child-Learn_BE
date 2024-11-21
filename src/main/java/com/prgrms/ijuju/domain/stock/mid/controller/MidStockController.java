//package com.prgrms.ijuju.domain.stock.mid.controller;
//
//import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockWithTradesResponse;
//import com.prgrms.ijuju.domain.stock.mid.service.MidStockService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class MidStockController {
//    private final MidStockService midStockService;
//
//
////    @GetMapping("/api/v1/mid-stock")
////    public ResponseEntity<List<MidStockWithTradesResponse>> getMemberStocksAndTrades(
////            @RequestParam int page,
////            @RequestParam int size
////    ) {
////        // 멤버아이디 토큰에서 받아야함
////        Long memberId = 1L;
////        Pageable pageable = PageRequest.of(page, size);
////        List<MidStockWithTradesResponse> response = midStockService.getMemberStocksAndTrades(memberId, pageable);
////        return ResponseEntity.ok(response);
////    }
//
//
//}
