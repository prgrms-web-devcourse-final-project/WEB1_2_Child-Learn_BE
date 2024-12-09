package com.prgrms.ijuju.domain.avatar.dto.response;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ItemResponseDTO {

    // 아이템 등록
    @Data
    public static class ItemRegisterResponseDTO {
        private Long id;
        private String name;
        private String imageUrl;
        private Long price;
        private String category;

        public ItemRegisterResponseDTO(Item item) {
            this.id = item.getId();
            this.name = item.getName();
            this.imageUrl = item.getImageUrl();
            this.price = item.getPrice();
            this.category = item.getCategory().getDisplayName();
        }
    }

    // 아이템 구매
    @Data
    @AllArgsConstructor
    public static class ItemPurchaseResponseDTO {
        private String message;
        private boolean isEquipped;
        private boolean isPurchased;
    }

    // 아이템 장착
    @Data
    @AllArgsConstructor
    public static class ItemEquipResponseDTO {
        private String message;
        private String itemImageUrl;
        private boolean isEquipped;
        private boolean isPurchased;
    }

    // 아이템 해제
    @Data
    @AllArgsConstructor
    public static class ItemRemoveResponseDTO {
        private String message;
        private String itemImageUrl;
        private boolean isEquipped;
        private boolean isPurchased;
    }

    // 아이템 조회
    @Data
    @AllArgsConstructor
    public static class ItemReadResponseDTO {
        private Long id;
        private String name;
        private Long price;
        private String imageUrl;
        private String description;
        private String category;

        public ItemReadResponseDTO(Item item) {
            this.id = item.getId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.imageUrl = item.getImageUrl();
            this.description = item.getDescription();
            this.category = item.getCategory().toString();
        }


    }

    // 아이템 목록 조회
    @Data
    @AllArgsConstructor
    public static class ItemReadAllResponseDTO {
        private Long id;
        private String name;
        private Long price;
        private String imageUrl;
        private String description;
        private String category;
        private boolean isEquipped; // 착용 여부
        private boolean isPurchased;


        public ItemReadAllResponseDTO(Item item, boolean isEquipped, boolean isPurchased) {
            this.id = item.getId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.imageUrl = item.getImageUrl();
            this.description = item.getDescription();
            this.category = item.getCategory().toString();
            this.isEquipped = isEquipped;
            this.isPurchased = isPurchased;
        }
    }

}
