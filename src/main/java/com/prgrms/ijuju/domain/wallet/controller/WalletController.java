package com.prgrms.ijuju.domain.wallet.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prgrms.ijuju.domain.wallet.dto.request.*;
import com.prgrms.ijuju.domain.wallet.dto.response.*;
import com.prgrms.ijuju.domain.wallet.service.WalletService;
import com.prgrms.ijuju.domain.wallet.service.TransactionService;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.global.auth.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    @GetMapping("/current")
    public ResponseEntity<WalletResponseDTO> showCurrentWallet(
            @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(walletService.showCurrentBalance(user.getId()));
    }

    @PostMapping("/exchange")
    public ResponseEntity<WalletResponseDTO> exchangePoints(
            @RequestBody ExchangeRequestDTO request) {
        return ResponseEntity.ok(walletService.exchangePointsToCoin(request));
    }

    @PostMapping("/attendance")
    public ResponseEntity<WalletResponseDTO> processAttendance(
            @RequestBody AttendanceRequestDTO request) {
        return ResponseEntity.ok(walletService.processAttendancePoints(request));
    }

    @PostMapping("/game")
    public ResponseEntity<WalletResponseDTO> processMiniGame(
            @RequestBody GamePointRequestDTO request) {
        return ResponseEntity.ok(walletService.processMiniGamePoints(request));
    }

    @PostMapping("/stock")
    public ResponseEntity<WalletResponseDTO> processStockInvestment(
            @RequestBody StockPointRequestDTO request) {
        return ResponseEntity.ok(walletService.simulateStockInvestment(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PointTransactionResponseDTO>> showTransactionHistory(
            @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(transactionService.showTransactionHistory(user.getId()));
    }

    @PostMapping("/history/transaction-type")
    public ResponseEntity<List<PointTransactionResponseDTO>> showTransactionByType(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody PointTransactionTypeRequestDTO request) {
        return ResponseEntity.ok(transactionService.showTransactionsByType(user.getId(), TransactionType.valueOf(request.getTransactionType())));
    }

    @PostMapping("/history/point-type")
    public ResponseEntity<List<PointTransactionResponseDTO>> showTransactionByPointType(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody PointTypeTransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.showTransactionsByPointType(user.getId(), PointType.valueOf(request.getPointType())));
    }

    @PostMapping("/history/weekly")
    public ResponseEntity<List<PointTransactionResponseDTO>> showWeeklyTransactions(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody PointTransactionWeeklyRequestDTO request) {
        LocalDateTime start = LocalDate.parse(request.getStartDate()).atStartOfDay();
        LocalDateTime end = start.plusDays(7);
        return ResponseEntity.ok(transactionService.showWeeklyTransactions(user.getId(), start, end));
    }

    @GetMapping("/history/exchange")
    public ResponseEntity<List<ExchangeTransactionResponseDTO>> showExchangeHistory(
            @AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok(transactionService.showExchangeHistory(user.getId()));
    }
} 
