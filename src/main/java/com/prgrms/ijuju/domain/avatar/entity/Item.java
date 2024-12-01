package com.prgrms.ijuju.domain.avatar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    public Item(String name, String description, Long price, ItemCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    @Builder
    public Item(String name, String description, Long price, ItemCategory category, List<Purchase> purchases) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.purchases = purchases;
    }
}
