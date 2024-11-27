package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventory")
public class Inventory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 해당 아이템을 보유한 회원

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  // 보유한 아이템

    private boolean isEquipped;

    public void changeEquippedStatus(boolean equipped) {

        if (this.isEquipped == equipped) {
            return;
        }

        this.isEquipped = equipped;
    }
}
