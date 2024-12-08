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

    private static final String SESSION_MEMBER_ID = "memberId";
    private static final String SESSION_GAME_STATE = "gameState";
    private static final int MAX_PHASE = 3;

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
        return playLimits.stream()
                .filter(limit -> limit.getDifficulty() == difficulty)
                .findFirst()
                .map(limit -> !limit.isPlayedToday(today))
                .orElse(true);
    }

    public WordQuizResponse startWordQuiz(HttpSession session, Long memberId, Difficulty difficulty) {
        Member member = getMemberOrThrowException(memberId);

        List<LimitWordQuiz> playLimits = limitWordQuizRepository.findByMember(member);
        LocalDate today = LocalDate.now();

        if (!isPlayAvailable(playLimits, difficulty, today)) {
            throw new WordQuizException(WordQuizErrorCode.DAILY_PLAY_LIMIT_EXCEEDED);
        }

        clearGameSession(session);

        WordQuiz quiz = wordQuizRepository.findRandomWord()
                .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.WORD_RETRIEVAL_FAILED));

        updateLastPlayedDate(member, difficulty, today);
        log.info("닉네임: {}, 난이도: {}, 낱말 게임 마지막 게임 날짜 갱신 완료", member.getUsername(), difficulty);

        WordQuizResponse gameResponse = WordQuizResponse.startNewGame(quiz, difficulty);
        session.setAttribute(SESSION_MEMBER_ID, memberId);
        session.setAttribute(SESSION_GAME_STATE, gameResponse);

        return gameResponse;
    }

    private void updateLastPlayedDate(Member member, Difficulty difficulty, LocalDate today) {
        LimitWordQuiz limitWordQuiz = limitWordQuizRepository.findByMemberAndDifficulty(member, difficulty)
                .orElseGet(() -> new LimitWordQuiz(member, difficulty, today));

        limitWordQuiz.changeLastPlayedDate(today);
        limitWordQuizRepository.save(limitWordQuiz);
    }

    public WordQuizResponse handleAnswer(Long memberId, HttpSession session, Boolean isCorrect) {
        Long sessionMemberId = (Long) session.getAttribute(SESSION_MEMBER_ID);
        if (sessionMemberId == null || !sessionMemberId.equals(memberId)) {
            throw new WordQuizException(WordQuizErrorCode.INVALID_USER);
        }

        WordQuizResponse gameState = (WordQuizResponse) session.getAttribute(SESSION_GAME_STATE);
        if (gameState == null) {
            throw new WordQuizException(WordQuizErrorCode.GAME_NOT_STARTED);
        }

        return isCorrect
                ? handleCorrectAnswer(session, gameState)
                : handleWrongAnswer(session, gameState);
    }

    private WordQuizResponse handleCorrectAnswer(HttpSession session, WordQuizResponse gameState) {
        if (gameState.currentPhase() < MAX_PHASE) {
            WordQuiz nextQuiz = wordQuizRepository.findRandomWord()
                    .orElseThrow(() -> new WordQuizException(WordQuizErrorCode.WORD_RETRIEVAL_FAILED));

            WordQuizResponse updatedGameState = gameState.withNewQuiz(nextQuiz)
                    .withUpdatedPhase(gameState.currentPhase() + 1);
            session.setAttribute(SESSION_GAME_STATE, updatedGameState);
            return updatedGameState;
        }

        session.removeAttribute(SESSION_GAME_STATE);
        return gameState.withGameOver(true);
    }

    private WordQuizResponse handleWrongAnswer(HttpSession session, WordQuizResponse gameState) {
        WordQuizResponse updatedGameState = gameState.withUpdatedLife(gameState.remainLife() - 1);
        if (updatedGameState.remainLife() <= 0) {
            session.removeAttribute(SESSION_GAME_STATE);
            return updatedGameState.withGameOver(true);
        }

        session.setAttribute(SESSION_GAME_STATE, updatedGameState);
        return updatedGameState;
    }

    private void clearGameSession(HttpSession session) {
        session.removeAttribute(SESSION_GAME_STATE);
        session.removeAttribute(SESSION_MEMBER_ID);
    }

}
