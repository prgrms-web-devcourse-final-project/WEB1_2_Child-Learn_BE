package com.prgrms.ijuju.domain.minigame.wordquiz.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizAvailabilityResponse;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizResponse;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.LimitWordQuiz;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizErrorCode;
import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizException;
import com.prgrms.ijuju.domain.minigame.wordquiz.repository.LimitWordQuizRepository;
import com.prgrms.ijuju.domain.minigame.wordquiz.repository.WordQuizRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WordQuizService {

    private final LimitWordQuizRepository limitWordQuizRepository;
    private final WordQuizRepository wordQuizRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public WordQuizAvailabilityResponse checkPlayAvailability(Long memberId) {
        Member member = getMemberOrThrowException(memberId);

        LocalDate today = LocalDate.now();
        List<LimitWordQuiz> playLimits = limitWordQuizRepository.findByMember(member);

        boolean isEasyPlay = isPlayAvailable(playLimits, Difficulty.EASY, today);
        boolean isNormalPlay = isPlayAvailable(playLimits, Difficulty.NORMAL, today);
        boolean isHardPlay = isPlayAvailable(playLimits, Difficulty.HARD, today);

        return new WordQuizAvailabilityResponse(isEasyPlay, isNormalPlay, isHardPlay);
    }

    private Member getMemberOrThrowException(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    String message = "회원을 찾을 수 없습니다. memberId : " + memberId;
                    log.error(message);
                    return new WordQuizException(WordQuizErrorCode.MEMBER_NOT_FOUND);
                });
    }

    private boolean isPlayAvailable(List<LimitWordQuiz> playLimits, Difficulty difficulty, LocalDate today) {
        if (playLimits == null) {
            log.error("난이도 {}의 마지막 게임 날짜가 null 입니다.", difficulty);
            throw new WordQuizException(WordQuizErrorCode.INVALID_PLAY_LIMITS);
        }
        return playLimits.stream()
                .filter(limit -> limit.getDifficulty() == difficulty)
                .findFirst()
                .map(limit -> !limit.isPlayedToday())
                .orElse(true);
    }

    @Transactional(readOnly = true)
    public WordQuizResponse startOrContinueWordQuiz(HttpSession session, Long memberId, Difficulty difficulty) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.MEMBER_NOT_FOUND));

        session.setAttribute("memberId", memberId);

        if (!isPlayAvailable(limitWordQuizRepository.findByMember(member), difficulty, LocalDate.now())) {
            throw new WordQuizException(WordQuizErrorCode.DAILY_PLAY_LIMIT_EXCEEDED);
        }

        WordQuizResponse gameResponse = (WordQuizResponse) session.getAttribute("gameState");
        if (gameResponse == null) {
            WordQuiz quiz = wordQuizRepository.findRandomWord()
                    .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.WORD_RETRIEVAL_FAILED));

            gameResponse = new WordQuizResponse(quiz.getWord(), quiz.getExplanation(), quiz.getHint(), 1, 3, difficulty);
            session.setAttribute("gameState", gameResponse);
        }

        return gameResponse;
    }

    @Transactional
    public WordQuizResponse handleAnswer(Long memberId, HttpSession session, Boolean isCorrect) {
        Long sessionMemberId = (Long) session.getAttribute("memberId");
        if (sessionMemberId == null || !sessionMemberId.equals(memberId)) {
            throw new WordQuizException(WordQuizErrorCode.INVALID_USER);
        }

        WordQuizResponse gameState = (WordQuizResponse) session.getAttribute("gameState");

        if (isCorrect) {
            if (gameState.currentPhase() < 3) {
                WordQuizResponse updatedGameState = gameState.withUpdatedPhase(gameState.currentPhase() + 1);
                session.setAttribute("gameState", updatedGameState);
                return updatedGameState;
            } else {
                updateLastPlayedDate(memberId, gameState.difficulty());
                return gameState;
            }
        } else {
            WordQuizResponse updatedGameState = gameState.withUpdatedLife(gameState.remainLife() - 1);
            session.setAttribute("gameState", updatedGameState);
            return updatedGameState;
        }
    }

    private void updateLastPlayedDate(Long memberId, Difficulty difficulty) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.MEMBER_NOT_FOUND));

        LimitWordQuiz limitWordQuiz = limitWordQuizRepository.findByMemberAndDifficulty(member, difficulty)
                .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.LIMIT_WORD_QUIZ_NOT_FOUND));

        limitWordQuiz.changeLastPlayedDate(LocalDate.now());
        limitWordQuizRepository.save(limitWordQuiz);
    }

}
