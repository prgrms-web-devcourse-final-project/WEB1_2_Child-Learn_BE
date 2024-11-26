package com.prgrms.ijuju.domain.avatar.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category; // "background" , "Pet" , "Hat"
    private String description;
    private Long price;
    private Boolean isEquipped;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;
}
