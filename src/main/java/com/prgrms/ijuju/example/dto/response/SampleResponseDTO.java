package com.prgrms.ijuju.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleResponseDTO {
    private Long ssn;
    private String name;
}
