package com.prgrms.ijuju.domain.avatar.controller;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.service.AvatarService;
import com.prgrms.ijuju.domain.avatar.service.ItemService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    private AvatarService avatarService;

    // 상품 구매
    @PostMapping("/purchase")
    public ResponseEntity<ItemResponseDTO.ItemPurchaseResponseDTO> purchase(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody ItemRequestDTO.ItemPurchaseRequestDTO dto) {

        long memberId = user.getId();
        ItemResponseDTO.ItemPurchaseResponseDTO responseDTO = itemService.purchaseItem(dto, memberId);

        return ResponseEntity.ok(responseDTO);
    }

    // 상품 장착
    @PostMapping("/isEquipped")
    public ResponseEntity<ItemResponseDTO.ItemEquipResponseDTO> isEquipped(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody ItemRequestDTO.ItemEquipRequestDTO dto) {

        long memberId = user.getId();
        ItemResponseDTO.ItemEquipResponseDTO responseDTO = avatarService.equipItem(memberId, dto.getId());

        return ResponseEntity.ok(responseDTO);
    }

}
