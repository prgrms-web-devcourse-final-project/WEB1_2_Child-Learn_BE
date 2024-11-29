package com.prgrms.ijuju.domain.avatar.dto.request;

import com.prgrms.ijuju.domain.avatar.entity.Inventory;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;

public class ItemRequestDTO {

    // 상품 등록
    @Data
    public static class ItemRegisterRequestDTO {
        private String name;
        private Long price;
        private String description;
        private String category;

        public ItemCategory getCategoryEnum() {
            return ItemCategory.valueOf(this.category.toUpperCase());
        }

        @Builder
        public Item toEntity() {  // 회원(owner)을 매개변수로 받아서 사용
            return Item.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .category(getCategoryEnum())
                    .purchases(new ArrayList<>())  // 소유자 추가
                    .build();
        }

    }

    // 상품 구매
    @Data
    public static class ItemPurchaseRequestDTO {
        private Long itemId;

    }

    @Data
    public static class ItemEquipRequestDTO {
        private Long id;
        private String name;
        private Long price;
        private String description;
        private String category;
    }
}
