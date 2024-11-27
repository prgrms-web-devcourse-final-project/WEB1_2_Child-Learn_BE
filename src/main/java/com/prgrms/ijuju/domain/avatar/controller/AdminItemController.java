package com.prgrms.ijuju.domain.avatar.controller;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.service.AdminItemService;
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
public class AdminItemController {

    @Autowired
    private AdminItemService adminItemService;

    // 상품 등록
    @PostMapping("/register")
    public ResponseEntity<ItemResponseDTO.ItemRegisterResponseDTO> register(@RequestBody ItemRequestDTO.ItemRegisterRequestDTO dto) {

        ItemResponseDTO.ItemRegisterResponseDTO registerDTO = adminItemService.registerItem(dto);

        return ResponseEntity.ok(registerDTO);
    }
}
