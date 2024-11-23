package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDetailsResponseDTO { // 포인트 상세 응답 DTO
    @NotNull
    private Long pointDetailsId;
    
    @NotNull
    private PointType pointType;
    
    @NotNull 
    private Long pointAmount;
    
    @NotNull
    private PointStatus pointStatus;
    
    @NotNull
    private LocalDateTime createdAt;

    public static PointDetailsResponseDTO from(PointDetails detail) { // PointDetails 엔티티를 DTO로 변환
        return new PointDetailsResponseDTO(
            detail.getId(),
            detail.getPointType(),
            detail.getPointAmount(), 
            detail.getPointStatus(),
            detail.getCreatedAt()
        );
    }
}