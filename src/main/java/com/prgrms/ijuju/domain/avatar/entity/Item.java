package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "member_items",  // 조인 테이블 이름
            joinColumns = @JoinColumn(name = "item_id"),  // 아이템이 속한 테이블에서 참조
            inverseJoinColumns = @JoinColumn(name = "member_id")  // 사용자 테이블에서 참조
    )
    private Set<Member> owners = new HashSet<>();

    public Item(String name, String description, Long price, ItemCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    @Builder
    public Item(String name, String description, Long price, ItemCategory category, Set<Member> owners) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.owners = owners != null ? owners : new HashSet<>();
    }
}
