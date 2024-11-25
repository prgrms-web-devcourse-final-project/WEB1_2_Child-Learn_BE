package com.prgrms.ijuju.domain.stock.begin.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "limit_begin_stock")
@Entity
public class LimitBeginStock {
    @Id
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Member player;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate lastPlayedDate;

    public LimitBeginStock(Member player) {
        this.player = player;
    }
}
