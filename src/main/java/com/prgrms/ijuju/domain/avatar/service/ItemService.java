package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.avatar.exception.ItemErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import com.prgrms.ijuju.domain.avatar.repository.PurchaseRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
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
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final MemberService memberService;

    // 상품 구매
    @Transactional
    public ItemResponseDTO.ItemPurchaseResponseDTO purchaseItem(ItemRequestDTO.ItemPurchaseRequestDTO dto, long memberId) {
        Member member = memberService.getMemberById(memberId);

        // 사려는 아이템 확인
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        Optional<Purchase> findPurchase = purchaseRepository.findByItemIdAndMemberId(dto.getItemId(), memberId);
        if (findPurchase.isPresent()) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_FOUND);
        }

        // 회원의 코인 확인
        if (member.getWallet().getCurrentCoins() < item.getPrice()) {
            throw new ItemException(ItemErrorCode.NOT_ENOUGH_COINS);
        }

        // 코인 차감
        member.getWallet().subtractCoins(item.getPrice());

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

}
