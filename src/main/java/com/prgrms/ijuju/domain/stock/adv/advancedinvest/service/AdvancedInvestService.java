package com.prgrms.ijuju.domain.stock.adv.advancedinvest.service;


import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.AdvancedInvestRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.AdvancedInvestResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.StockResponseDto;

import java.util.List;

public interface AdvancedInvestService {
    AdvancedInvestResponseDto startGame(AdvancedInvestRequestDto request);

    void pauseGame(Long advId);

    void resumeGame(Long advId);

    void endGame(Long advId);

    List<StockResponseDto> getReferenceData(Long advId);

    StockResponseDto getLiveData(Long advId, String symbol, int hour);

    void buyStock(Long advId, StockTransactionRequestDto request);

    void sellStock(Long advId, StockTransactionRequestDto request);
}
