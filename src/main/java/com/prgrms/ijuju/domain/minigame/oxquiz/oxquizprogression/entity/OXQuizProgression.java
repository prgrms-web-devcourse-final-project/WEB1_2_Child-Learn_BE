package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "quiz_progression")
public class OXQuizProgression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private LocalDate lastPlayedDate;

}