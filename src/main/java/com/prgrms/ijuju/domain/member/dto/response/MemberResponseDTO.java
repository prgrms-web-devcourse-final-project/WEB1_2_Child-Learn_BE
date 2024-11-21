package com.prgrms.ijuju.domain.member.dto.response;

public class MemberResponseDTO {

    public static class CreateResponseDTO {
        private String message;

        public CreateResponseDTO(String message) {
            this.message = message;
        }
    }
}
