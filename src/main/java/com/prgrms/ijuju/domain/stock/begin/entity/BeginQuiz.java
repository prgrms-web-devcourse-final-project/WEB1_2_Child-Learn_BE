package com.prgrms.ijuju.domain.stock.begin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "begin_quiz")
@Entity
public class BeginQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String oContent;

    @Column(nullable = false)
    private String xContent;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate createdDate;

    @Builder
    public BeginQuiz(String content, String oContent, String xContent, String answer) {
        this.content = content;
        this.oContent = oContent;
        this.xContent = xContent;
        this.answer = answer;
    }
}


