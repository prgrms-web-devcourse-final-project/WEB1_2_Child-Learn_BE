package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.avatar.exception.AvatarErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.AvatarException;
import com.prgrms.ijuju.domain.avatar.exception.ItemErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.AvatarRepository;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import com.prgrms.ijuju.domain.avatar.repository.PurchaseRepository;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;
    private final PurchaseRepository purchaseRepository;

    // 아이템 장착
    @Transactional
    public ItemResponseDTO.ItemEquipResponseDTO equipItem(
            ItemRequestDTO.ItemEquipRequestDTO dto,
            Long memberId) {

        // 1. 아바타 조회
        Avatar avatar = avatarRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AvatarException(AvatarErrorCode.AVATAR_NOT_FOUND));

        // 2. 아이템 조회
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        // 3. 아이템 장착
        switch (item.getCategory()) {
            case HAT -> avatar.changeHat(item);
            case PET -> avatar.changePet(item);
            case BACKGROUND -> avatar.changeBackground(item);
            default -> throw new ItemException(ItemErrorCode.INAVALID_ITEM_CATEGORY);
        }

        // 4. 아바타 저장
        avatarRepository.save(avatar);

        Optional<Purchase> purchase = purchaseRepository.findByItemIdAndMemberId(item.getId(), memberId);

        if (purchase.isPresent()) {
            purchase.get().changeIsEquipped(true);
        }

        // 아이템 이미지 URL 반환(프론트에서 합성할 수 있도록)
        String itemImageUrl = "/uploads/" + item.getImageUrl();

        return new ItemResponseDTO.ItemEquipResponseDTO("아이템이 장착되었습니다", itemImageUrl);
    }

    // 아이템 해제
    @Transactional
    public ItemResponseDTO.ItemRemoveResponseDTO removeItem(
            ItemRequestDTO.ItemRemoveRequestDTO dto,
            Long memberId) {

        // 1. 아바타 조회
        Avatar avatar = avatarRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AvatarException(AvatarErrorCode.AVATAR_NOT_FOUND));

        // 2. 아이템 조회
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        log.info("해제할 아이템을 찾습니다");

        // 3. 아이템 해제
        switch (item.getCategory()) {
            case HAT -> avatar.removeHat();
            case PET -> avatar.removePet();
            case BACKGROUND -> avatar.removeBackground();
            default -> throw new ItemException(ItemErrorCode.INAVALID_ITEM_CATEGORY);
        }

        Optional<Purchase> purchase = purchaseRepository.findByItemIdAndMemberId(item.getId(), memberId);

        if (purchase.isPresent()) {
            purchase.get().changeIsEquipped(false);
        }

        // 아이템 이미지 URL 반환(프론트에서 합성할 수 있도록)
        String itemImageUrl = "/uploads/default-image.png";

        if (avatar.getHat() != null || avatar.getPet() != null || avatar.getBackground() != null) {
            itemImageUrl = combineItemImages(avatar);   // 남아있는 아이템들로 합성된 이미지 생성
        }

        return new ItemResponseDTO.ItemRemoveResponseDTO("아이템이 성공적으로 해제되었습니다.", itemImageUrl);
    }

    // 아이템 합성
    private String combineItemImages(Avatar avatar) {

        String imageUrl = "/uploads/";

        if (avatar.getBackground() != null) {
            imageUrl += avatar.getBackground().getImageUrl();
        }
        if (avatar.getHat() != null) {
            imageUrl += avatar.getHat().getImageUrl();
        }
        if (avatar.getPet() != null) {
            imageUrl += avatar.getPet().getImageUrl();
        }

        // 최종 합성된 이미지 URL 반환
        return imageUrl;
    }
}
