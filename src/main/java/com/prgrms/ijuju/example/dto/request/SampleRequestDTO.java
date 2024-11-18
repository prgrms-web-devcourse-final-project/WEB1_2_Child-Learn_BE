package com.prgrms.ijuju.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleRequestDTO {
    private Long ssn;
    private String name;
}
