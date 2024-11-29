//package com.prgrms.ijuju.domain.avatar.entity;
//
//import com.prgrms.ijuju.domain.member.entity.Member;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "inventory")
//public class Inventory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;  // 해당 아이템을 보유한 회원
//
//    @ManyToOne
//    @JoinColumn(name = "item_id")
//    private Item item;  // 보유한 아이템
//
//    private boolean isEquipped;
//
//    private LocalDateTime purchasedAt;
//
//    @Builder
//    public Inventory(Member member, Item item, boolean isEquipped, LocalDateTime purchasedAt) {
//        this.member = member;
//        this.item = item;
//        this.isEquipped = isEquipped;
//        this.purchasedAt = purchasedAt;
//    }
//
//    public void changeEquippedStatus(boolean equipped) {
//
//        if (this.isEquipped == equipped) {
//            return;
//        }
//
//        this.isEquipped = equipped;
//    }
//}
