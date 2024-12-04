package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository.OXQuizDataRepository;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Priority;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.repository.OXQuizProgressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OXQuizProgressionServiceImpl implements OXQuizProgressionService {

    private final OXQuizProgressionRepository progressionRepository;
    private final OXQuizDataRepository oxQuizDataRepository;

    @Override
    public List<OXQuizProgression> getProgressionsByMemberId(Long memberId) {
        return progressionRepository.findByMemberId(memberId);
    }

    @Override
    public QuizResponseDto updateProgression(Long progressionId, String userAnswer) {
        // 진행 상태 조회
        OXQuizProgression progression = progressionRepository.findById(progressionId)
                .orElseThrow(() -> new IllegalArgumentException("Progression not found"));

        // 정답과 비교
        boolean isCorrect = progression.getOxQuizData().getAnswer().equalsIgnoreCase(userAnswer);

        // priority 업데이트
        if (isCorrect) {
            progression.setPriority(Priority.LOW);
        } else {
            progression.setPriority(Priority.HIGH);
        }

        // DB에 저장
        progressionRepository.save(progression);

        // 설명만 포함한 DTO 반환 (question, difficulty 제외)
        return new QuizResponseDto(
                progression.getId(),
                progression.getOxQuizData().getId(),
                progression.getOxQuizData().getExplanation(),
                isCorrect
        );
    }


    @Override
    public List<QuizResponseDto> getQuizzesForUser(Long memberId, String difficulty) {
        // 기존 진행 상태 가져오기
        List<OXQuizProgression> progressions = progressionRepository.findByMemberId(memberId);

        // 진행 상태와 새 문제 분류
        Map<Priority, List<OXQuizProgression>> prioritizedProgressions = groupProgressionsByPriority(progressions, difficulty);

        // 출제율 가중치 적용
        List<OXQuizProgression> weightedProgressions = applyWeighting(prioritizedProgressions);

        // 최종 문제 리스트 생성
        Collections.shuffle(weightedProgressions); // 문제 순서를 섞음

        // DTO로 변환 후 반환
        return weightedProgressions.stream()
                .limit(3)
                .map(prog -> new QuizResponseDto(
                        prog.getId(),                              // progressionId
                        prog.getOxQuizData().getId(),              // oxQuizDataId
                        prog.getOxQuizData().getExplanation(),     // explanation
                        prog.getOxQuizData().getAnswer().equals(prog.getOxQuizData().getAnswer()) // isCorrect
                ))
                .toList();
    }

    private Map<Priority, List<OXQuizProgression>> groupProgressionsByPriority(List<OXQuizProgression> progressions, String difficulty) {
        Map<Priority, List<OXQuizProgression>> priorityMap = new HashMap<>();

        // Priority 별로 진행 상태 분류
        for (Priority priority : Priority.values()) {
            List<OXQuizProgression> filteredProgressions = progressions.stream()
                    .filter(prog -> prog.getPriority() == priority && prog.getOxQuizData().getDifficulty().equalsIgnoreCase(difficulty))
                    .toList();
            priorityMap.put(priority, filteredProgressions);
        }

        // 새로운 문제 추가
        List<OXQuizProgression> newProgressions = oxQuizDataRepository.findAll().stream()
                .filter(quiz -> quiz.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(quiz -> progressions.stream()
                        .noneMatch(prog -> prog.getOxQuizData().getId().equals(quiz.getId())))
                .map(quiz -> new OXQuizProgression(null, null, quiz, null, Priority.HIGH, LocalDate.now()))
                .toList();

        priorityMap.put(null, newProgressions); // 새로운 문제는 Priority 없음
        return priorityMap;
    }

    private List<OXQuizProgression> applyWeighting(Map<Priority, List<OXQuizProgression>> prioritizedProgressions) {
        List<OXQuizProgression> weightedProgressions = new ArrayList<>();

        // HIGH Priority: 2배
        prioritizedProgressions.getOrDefault(Priority.HIGH, Collections.emptyList())
                .forEach(prog -> {
                    weightedProgressions.add(prog);
                    weightedProgressions.add(prog); // 2배
                });

        // LOW Priority: 50% 확률로 추가
        List<OXQuizProgression> lowPriority = prioritizedProgressions.getOrDefault(Priority.LOW, Collections.emptyList());
        lowPriority.stream()
                .filter(prog -> new Random().nextBoolean()) // 50% 확률로 추가
                .forEach(weightedProgressions::add);

        // 새로운 문제: 1배
        weightedProgressions.addAll(prioritizedProgressions.getOrDefault(null, Collections.emptyList()));

        return weightedProgressions;
    }
}