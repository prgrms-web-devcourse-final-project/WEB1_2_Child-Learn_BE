package com.prgrms.ijuju.domain.point.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GamePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;

    @NotNull
    private GameType gameType;

    @NotNull
    private Long points_earned;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isWin;
} 