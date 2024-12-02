package com.prgrms.ijuju.domain.avatar.dto.request;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import lombok.Builder;
import lombok.Data;

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
                    //.purchases(new ArrayList<>())  // 소유자 추가
                    .build();
        }

    }

    // 상품 구매
    @Data
    public static class ItemPurchaseRequestDTO {
        private Long itemId;

    }

    // 아이템 장착
    @Data
    public static class ItemEquipRequestDTO {
        private Long memberId;
        private Long itemId;
//        private String name;
//        private Long price;
//        private String description;
//        private String category;
    }

    // 아이템 해제
    @Data
    public static class ItemRemoveRequestDTO {
        private Long itemId;
    }
}
