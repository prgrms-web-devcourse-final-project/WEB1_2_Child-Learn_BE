package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OXQuizProgression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "ox_quiz_data_id", nullable = false)
    private OXQuizData oxQuizData;

    private Boolean isCorrect;

    @Enumerated(EnumType.STRING)
    private Priority priority; // LOW, HIGH

    private LocalDate attemptDate;
}