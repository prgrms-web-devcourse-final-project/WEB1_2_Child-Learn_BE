package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Getter
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private boolean isEquipped = false;
    private boolean isOwned = false;

    private LocalDateTime purchaseDate;

    @Builder
    public Purchase(Member member, Item item, boolean isOwned, LocalDateTime purchaseDate) {
        this.member = member;
        this.item = item;
        this.isOwned  = isOwned;
        this.purchaseDate = purchaseDate;
    }

    public void changeEquippedStatus(boolean equipped) {

        if (this.isEquipped == equipped) {
            return;
        }

        this.isEquipped = equipped;
    }
}
