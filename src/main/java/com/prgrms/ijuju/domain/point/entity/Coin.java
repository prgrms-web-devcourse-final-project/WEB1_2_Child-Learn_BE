package com.prgrms.ijuju.domain.point.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotNull;

import com.prgrms.ijuju.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private Long currentCoins;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addCoins(Long coins) {
        this.currentCoins += coins;
    }

    public void deductCoins(Long coins) {
        this.currentCoins -= coins;
    }
}
