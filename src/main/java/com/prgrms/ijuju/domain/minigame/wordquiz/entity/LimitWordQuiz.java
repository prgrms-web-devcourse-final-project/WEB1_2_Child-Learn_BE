package com.prgrms.ijuju.domain.minigame.wordquiz.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "limit_word_quiz",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "difficulty"})}
)
public class LimitWordQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    private LocalDate lastPlayedDate;

    @Builder
    public LimitWordQuiz(Member member, Difficulty difficulty, LocalDate lastPlayedDate) {
        this.member = member;
        this.difficulty = difficulty;
        this.lastPlayedDate = lastPlayedDate != null ? lastPlayedDate : LocalDate.now().minusDays(1);
    }

    public boolean isPlayedToday(LocalDate today) {
        return this.lastPlayedDate.equals(today);
    }

    public void changeLastPlayedDate(LocalDate newLastPlayedDate) {
        this.lastPlayedDate = newLastPlayedDate;
    }
}
