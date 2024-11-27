package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "avatar")
public class Avatar extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "background_id")
    private Item background;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Item pet;

    @OneToOne
    @JoinColumn(name = "hat_id")
    private Item hat;

    public void changeBackground(Item background) {
        if (background.getCategory() != ItemCategory.BACKGROUND) {
            throw new IllegalArgumentException("Item category must be BACKGROUND for background.");
        }
        this.background = background;
    }

    public void changePet(Item pet) {
        if (pet.getCategory() != ItemCategory.PET) {
            throw new IllegalArgumentException("Item category must be PET for pet.");
        }
        this.pet = pet;
    }

    public void changeHat(Item hat) {
        if (hat.getCategory() != ItemCategory.HAT) {
            throw new IllegalArgumentException("Item category must be HAT for hat.");
        }
        this.hat = hat;
    }
}
