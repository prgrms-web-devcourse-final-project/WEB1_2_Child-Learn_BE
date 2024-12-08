package com.prgrms.ijuju.domain.avatar.service;

import com.prgrms.ijuju.domain.avatar.dto.request.ItemRequestDTO;
import com.prgrms.ijuju.domain.avatar.dto.response.ItemResponseDTO;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.avatar.exception.ItemErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.avatar.repository.ItemRepository;
import com.prgrms.ijuju.domain.avatar.repository.PurchaseRepository;
import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        return new ItemResponseDTO.ItemPurchaseResponseDTO("아이템을 구매했습니다");

    }

    // 아이템 이미지 조회
    public ItemResponseDTO.ItemReadResponseDTO readItem(Long id, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Item items = itemRepository.findById(id)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        return new ItemResponseDTO.ItemReadResponseDTO(items);
    }

    // 아이템 목록 조희
    public Page<ItemResponseDTO.ItemReadAllResponseDTO> readItemAll(
            MemberRequestDTO.PageRequestDTO dto,
            Long memberId) {
        Pageable pageable = dto.getPageable();

        Page<Item> itemPage = itemRepository.findAll(pageable);

        return itemPage.map(ItemResponseDTO.ItemReadAllResponseDTO::new);
    }

}
