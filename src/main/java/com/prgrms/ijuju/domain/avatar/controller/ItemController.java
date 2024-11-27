package com.prgrms.ijuju.domain.avatar.controller;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member/avatar")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    // 상품 구매
    @PostMapping("/purchase")
    public ResponseEntity<ItemResponseDTO.ItemPurchaseResponseDTO> purchase(@RequestBody ItemRequestDTO.ItemPurchaseRequestDTO dto) {

        ItemResponseDTO.ItemPurchaseResponseDTO responseDTO = itemService.purchaseItem(dto);

        return ResponseEntity.ok(responseDTO);
    }

}
