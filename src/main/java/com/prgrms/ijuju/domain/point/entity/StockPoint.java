package com.prgrms.ijuju.domain.point.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StockPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;
    
    @Setter
    @NotNull
    private StockType stockType;
    
    @Setter
    @NotNull
    private String stockName;

    @Setter
    @NotNull
    private StockStatus stockStatus;

    @Setter
    @NotNull
    private Long points_earned;

    @Setter
    @NotNull
    private Long points_spent;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;
} 
