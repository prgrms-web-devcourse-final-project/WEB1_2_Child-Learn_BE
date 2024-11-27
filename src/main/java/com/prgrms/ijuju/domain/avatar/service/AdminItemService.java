package com.prgrms.ijuju.domain.avatar.service;

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
//
//    // 아이템 등록
//    public ItemResponseDTO.ItemRegisterResponseDTO register(ItemRequestDTO.ItemRegisterRequestDTO dto) {
//
//        // 아이템이 이미 존재하는지 확인
//        if (itemRepository.existsByName(dto.getName())) {
//            throw ItemException.ITEM_IS_ALREADY_REGISTED.getItemTaskException();
//        }
//
//
//
//        return itemRepository.save(newItem);
//    }
}
