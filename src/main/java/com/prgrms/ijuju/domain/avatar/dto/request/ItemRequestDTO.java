package com.prgrms.ijuju.domain.avatar.dto.request;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.Data;

public class ItemRequestDTO {

    // 상품 등록
    @Data
    public static class ItemRegisterRequestDTO {
        private String name;
        private Long price;
        private String description;
        private Boolean isEquipped;
        private ItemCategory category;
    }

    // 상품 구매
    @Data
    public static class ItemPurchaseRequestDTO {
        private Member member;
        private Long id;

    }
}
