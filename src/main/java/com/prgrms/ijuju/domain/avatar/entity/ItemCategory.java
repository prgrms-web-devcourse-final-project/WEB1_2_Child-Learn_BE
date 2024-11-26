package com.prgrms.ijuju.domain.avatar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_category")
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // Background, Pet, Hat




}
