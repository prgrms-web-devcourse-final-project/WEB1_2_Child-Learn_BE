package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.exception.AvatarException;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.AvatarRepository;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final ItemRepository itemRepository;

    // 아이템 장착
    @Transactional
    public ItemResponseDTO.ItemEquipResponseDTO equipItem(Long memberId, Long itemId) {
        Avatar avatar = avatarRepository.findByMemberId(memberId)
                .orElseThrow(() -> AvatarException.AVATAR_NOT_FOUND.getItemTaskException());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemException.ITEM_NOT_FOUND.getItemTaskException());

        switch (item.getCategory()) {
            case HAT -> avatar.changeHat(item);
            case PET -> avatar.changePet(item);
            case BACKGROUND -> avatar.changeBackground(item);
            default -> throw ItemException.INAVALID_ITEM_CATEGORY.getItemTaskException();
        }

        avatarRepository.save(avatar);

        return new ItemResponseDTO.ItemEquipResponseDTO("아이템이 장착되었습니다");
    }




}
