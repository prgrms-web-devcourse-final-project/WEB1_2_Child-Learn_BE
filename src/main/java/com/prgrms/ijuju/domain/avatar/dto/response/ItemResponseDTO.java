package com.prgrms.ijuju.domain.avatar.dto.response;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import lombok.Data;

public class ItemResponseDTO {

    // 상품 등록
    @Data
    public static class ItemRegisterResponseDTO {
        private Long id;
        private String name;
        private Long price;
        private String category;

        public ItemRegisterResponseDTO(Item item) {
            this.id = item.getId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.category = item.getCategory().getDisplayName();
        }
    }

    // 상품 구매
    @Data
    public static class ItemPurchaseResponseDTO {
        private String message;

        public ItemPurchaseResponseDTO(String message) {
            this.message = message;
        }
    }
}
