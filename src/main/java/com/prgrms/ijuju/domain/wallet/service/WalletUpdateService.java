package com.prgrms.ijuju.domain.wallet.service;

import com.prgrms.ijuju.domain.wallet.dto.response.WalletResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletUpdateService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyWalletUpdate(Long memberId, WalletResponseDTO walletUpdate) {
        String destination = "/topic/wallet/" + memberId;
        messagingTemplate.convertAndSend(destination, walletUpdate);
    }
} 