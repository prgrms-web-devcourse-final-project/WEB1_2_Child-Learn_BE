package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    private LocalDateTime purchaseDate;

    private boolean isEquipped; // 구매 시 장착여부

    @Builder
    public Purchase(Member member, Item item, LocalDateTime purchaseDate, boolean isEquipped) {
        this.member = member;
        this.item = item;
        this.purchaseDate = purchaseDate;
        this.isEquipped = isEquipped;
    }
}
