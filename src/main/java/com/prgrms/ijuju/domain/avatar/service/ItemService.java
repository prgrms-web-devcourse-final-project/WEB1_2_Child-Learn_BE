package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import com.prgrms.ijuju.domain.avatar.repository.PurchaseRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final MemberService memberService;

    // 상품 구매
    @Transactional
    public ItemResponseDTO.ItemPurchaseResponseDTO purchaseItem(ItemRequestDTO.ItemPurchaseRequestDTO dto, long memberId) {
        Member member = memberService.getMemberById(memberId);

        // 사려는 아이템 확인
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> ItemException.ITEM_NOT_FOUND.getItemTaskException());

        Optional<Purchase> findPurchase = purchaseRepository.findByItemIdAndMemberId(dto.getItemId(), memberId);
        if (findPurchase.isPresent()) {
            throw ItemException.ITEM_IS_ALREADY_PURCHASED.getItemTaskException();
        }

        // 회원의 코인 확인
        if (member.getCoins() < item.getPrice()) {
            throw ItemException.NOT_ENOUGH_COINS.getItemTaskException();
        }

        member.getRemainingCoins(member.getCoins(), item.getPrice());

        Purchase newPurchase = Purchase.builder()
                .member(member)
                .item(item)
                .purchaseDate(LocalDateTime.now())
                .isEquipped(false)
                .build();
        log.info("아이템 구매 완료");

        purchaseRepository.save(newPurchase);

//        // 구매한 아이템 이벤토리에 추가
//        Inventory newInventory = Inventory.builder()
//                .member(member)
//                .item(item)
//                .isEquipped(false)
//                .purchasedAt(LocalDateTime.now())
//                .build();
//
//        inventoryRepository.save(newInventory);

        return new ItemResponseDTO.ItemPurchaseResponseDTO("아이템을 구매했습니다");

    }

    // 상품 장착(수정중)
    public void equipItemToAvatar(Avatar avatar, Item item) {
        if (item.getCategory() == ItemCategory.BACKGROUND) {
            avatar.changeBackground(item);
        } else if (item.getCategory() == ItemCategory.PET) {
            avatar.changePet(item);
        } else if (item.getCategory() == ItemCategory.HAT) {
            avatar.changeHat(item);
        } else {
            ItemException.INAVALID_ITEM_CATEGORY.getItemTaskException();
        }
    }

    // 아이템 해제

    //


}
