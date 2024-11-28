package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long price;

    @Enumerated(EnumType.STRING)    // 'enum'을 문자열로 저장
    private ItemCategory category;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Builder
    public Item(String name, String description, Long price, ItemCategory category, Member owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.owner = owner;
    }
}
