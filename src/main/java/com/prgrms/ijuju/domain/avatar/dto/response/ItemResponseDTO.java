package com.prgrms.ijuju.domain.avatar.dto.response;

import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import lombok.Data;

public class ItemResponseDTO {

    // 상품 등록
    @Data
    public static class ItemRegisterResponseDTO {
        private String name;
        private Long price;
        private ItemCategory category;
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
