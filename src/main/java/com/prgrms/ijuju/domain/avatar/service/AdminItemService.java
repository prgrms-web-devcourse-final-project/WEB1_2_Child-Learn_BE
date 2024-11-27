package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminItemService {

    private final ItemRepository itemRepository;

    // 아이템 등록
    @Transactional
    public ItemResponseDTO.ItemRegisterResponseDTO registerItem(ItemRequestDTO.ItemRegisterRequestDTO dto) {
        try {
            if (itemRepository.existsByName(dto.getName())) {
                throw ItemException.ITEM_IS_ALREADY_REGISTED.getItemTaskException();
            }

            // DTO 를 엔티티로 변환
            Item newItem = dto.toEntity();

            // 아이템 저장
            Item savedItem = itemRepository.save(newItem);

            // 저장한 아이템을 응답 DTO로 변화하여 반환
            return new ItemResponseDTO.ItemRegisterResponseDTO(savedItem);

        } catch (Exception e) {
            throw ItemException.ITEM_NOT_REGISTERED.getItemTaskException();
        }
    }
}
