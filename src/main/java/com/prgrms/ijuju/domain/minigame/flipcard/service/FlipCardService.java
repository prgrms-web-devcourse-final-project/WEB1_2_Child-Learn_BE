package com.prgrms.ijuju.domain.minigame.flipcard.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.FlipCardResponse;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.PlayFlipCardAvailable;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.LimitCardGame;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.FlipCardRepository;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.LimitCardGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FlipCardService {
    private final FlipCardRepository flipCardRepository;
    private final LimitCardGameRepository limitCardGameRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<FlipCardResponse> findRandomCards(String difficulty) {
        log.info("랜덤 카드 찾기");
        List<FlipCard> allCards = flipCardRepository.findAll();
        int count = calculateCardNumber(difficulty);

        Collections.shuffle(allCards);
        return allCards.subList(0, count).stream()
                .map(FlipCardResponse::of)
                .collect(Collectors.toList());
    }

    public void saveOrUpdatePlay(Long memberId, String difficulty) {
        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        // LimitCardGame 조회 또는 생성
        LimitCardGame limitCardGame = limitCardGameRepository.findById(memberId)
                .orElseGet(() -> {
                    log.info("첫 플레이 기록 생성: memberId = {}", memberId);
                    return limitCardGameRepository.save(LimitCardGame.builder().member(member).build());
                });

        // 난이도별 마지막 플레이 시간 갱신
        updatePlayTime(limitCardGame, difficulty);
    }

    @Transactional(readOnly = true)
    public PlayFlipCardAvailable checkPlayAvailable(Long memberId) {
        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        return limitCardGameRepository.findById(memberId)
                .map(limitCardGame -> {
                    boolean isBegin = limitCardGame.getBeginLastPlayed().isBefore(LocalDate.now());
                    boolean isMid = limitCardGame.getMidLastPlayed().isBefore(LocalDate.now());
                    boolean isAdv = limitCardGame.getAdvLastPlayed().isBefore(LocalDate.now());
                    return PlayFlipCardAvailable.of(isBegin, isMid, isAdv);
                })
                .orElseGet(() -> PlayFlipCardAvailable.of(true, true, true));
    }

    private void updatePlayTime(LimitCardGame limitCardGame, String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "begin":
                limitCardGame.updateBeginLastPlayed();
                break;
            case "mid":
                limitCardGame.updateMidLastPlayed();
                break;
            case "adv":
                limitCardGame.updateAdvLastPlayed();
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }

    private int calculateCardNumber(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "begin" -> 4;
            case "mid" -> 6;
            case "adv" -> 8;
            default -> throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        };
    }
}
