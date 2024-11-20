package com.prgrms.ijuju.point.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PointResponseDTO {
    private Long currentPoints;
    private Long currentCoins;
    private List<PointDetailsDTO> history;
}

