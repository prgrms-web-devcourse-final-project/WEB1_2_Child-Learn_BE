package com.prgrms.ijuju.domain.article.entity;

import com.prgrms.ijuju.domain.article.contant.DataType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private String trendPrediction;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType type;

    public void decreaseDuration() {
        if (duration > 0) {
            duration--;
        }
    }

}