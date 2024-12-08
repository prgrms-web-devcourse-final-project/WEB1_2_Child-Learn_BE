package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 구매한 회원

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  // 구매한 아이템

    @CreatedDate
    private LocalDateTime purchaseDate;

    private boolean isEquipped; // 구매 시 장착여부

    @Builder
    public Purchase(Member member, Item item, LocalDateTime purchaseDate) {
        this.member = member;
        this.item = item;
        this.isEquipped = false;
        this.purchaseDate = purchaseDate;
    }

    public void changeIsEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }
}
