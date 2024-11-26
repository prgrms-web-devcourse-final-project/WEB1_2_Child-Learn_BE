package com.prgrms.ijuju.domain.minigame.flipcard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "cardTitle", "cardContent", "cardCategory"})
public class FlipCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardTitle;

    @Column(nullable = false)
    private String cardContent;

    @Column(nullable = false)
    private String cardCategory;

    @Builder
    public FlipCard(Long id, String cardTitle, String cardContent, String cardCategory) {
        this.id = id;
        this.cardTitle = cardTitle;
        this.cardContent = cardContent;
        this.cardCategory = cardCategory;
    }
}
