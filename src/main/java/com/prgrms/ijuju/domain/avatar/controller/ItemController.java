package com.prgrms.ijuju.domain.avatar.controller;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.service.AvatarService;
import com.prgrms.ijuju.domain.avatar.service.ItemService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member/avatar")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final AvatarService avatarService;

    // 아이템 구매
    @PostMapping("/purchase")
    public ResponseEntity<ItemResponseDTO.ItemPurchaseResponseDTO> purchase(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody ItemRequestDTO.ItemPurchaseRequestDTO dto) {

        long memberId = user.getId();
        ItemResponseDTO.ItemPurchaseResponseDTO responseDTO = itemService.purchaseItem(dto, memberId);

        return ResponseEntity.ok(responseDTO);
    }

    // 아이템 장착
    @PostMapping("/isEquipped")
    public ResponseEntity<ItemResponseDTO.ItemEquipResponseDTO> isEquipped(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody ItemRequestDTO.ItemEquipRequestDTO dto) {

        long memberId = user.getId();
        ItemResponseDTO.ItemEquipResponseDTO responseDTO = avatarService.equipItem(dto, memberId);

        return ResponseEntity.ok(responseDTO);
    }


    // 아이템 해제
    @PostMapping("/remove")
    public ResponseEntity<ItemResponseDTO.ItemRemoveResponseDTO> removeItem(
            @AuthenticationPrincipal SecurityUser user,
            @Validated @RequestBody ItemRequestDTO.ItemRemoveRequestDTO dto) {
        long memberId = user.getId();
        ItemResponseDTO.ItemRemoveResponseDTO responseDTO = avatarService.removeItem(dto, memberId);

        return ResponseEntity.ok(responseDTO);
    }

    // 아이템 목록 조회
    @PostMapping("/read")
    public ResponseEntity<ItemResponseDTO.ItemReadResponseDTO> readItem(@RequestBody Long id,
                                                                        @AuthenticationPrincipal SecurityUser user) {

        Long memberId = user.getId();
        ItemResponseDTO.ItemReadResponseDTO response = itemService.readItem(id, memberId);

        return ResponseEntity.ok(response);
    }


}
