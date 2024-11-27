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

    @OneToOne(mappedBy = "item")
    private Purchase purchase;

    @Builder
    public Item(String name, String description, Long price, ItemCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }


}
