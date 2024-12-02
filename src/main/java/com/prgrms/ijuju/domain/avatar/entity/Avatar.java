package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "background_id")
    private Item background;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Item pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hat_id")
    private Item hat;

    public Avatar(Member member) {
        this.member = member;
    }

    @Builder
    public Avatar(Member member, Item background, Item pet, Item hat) {
        this.member = member;
        this.background = background;
        this.pet = pet;
        this.hat = hat;
    }

    public Avatar changeBackground(Item background) {
        if (background.getCategory() != ItemCategory.BACKGROUND) {
            throw new IllegalArgumentException("배경 카테고리가 아닙니다.");
        }
        this.background = background;
        return this;
    }

    public Avatar changePet(Item pet) {
        if (pet.getCategory() != ItemCategory.PET) {
            throw new IllegalArgumentException("펫 카테고리가 아닙니다.");
        }
        this.pet = pet;
        return this;
    }

    public Avatar changeHat(Item hat) {
        if (hat.getCategory() != ItemCategory.HAT) {
            throw new IllegalArgumentException("모자 카테고리가 아닙니다.");
        }
        this.hat = hat;
        return this;
    }

    public void removeHat() {
        this.hat = null;
    }

    public void removePet() {
        this.pet = null;
    }

    public void removeBackground() {
        this.background = null;
    }

}
