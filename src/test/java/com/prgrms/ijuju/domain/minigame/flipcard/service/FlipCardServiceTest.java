package com.prgrms.ijuju.domain.minigame.flipcard.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.FlipCardResponse;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.PlayFlipCardAvailable;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.LimitCardGame;
import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardDfficultyNotFoundException;
import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardMemberNotFoundException;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.FlipCardRepository;
import com.prgrms.ijuju.domain.minigame.flipcard.repository.LimitCardGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class FlipCardServiceTest {

    @InjectMocks
    private FlipCardService flipCardService;

    @Mock
    private FlipCardRepository flipCardRepository;

    @Mock
    private LimitCardGameRepository limitCardGameRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member testMember;
    private List<FlipCard> testCards;
    private LimitCardGame limitCardGame;

    @BeforeEach
    void setUp() {
        // 테스트 멤버 설정
        testMember = Member.builder()
                .loginId("testUser")
                .username("테스트유저")
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .points(1000L)
                .coins(1000L)
                .build();

        // 테스트용 카드 데이터 설정
        testCards = Arrays.asList(
                FlipCard.builder()
                        .id(1L)
                        .cardTitle("카드1")
                        .cardContent("내용1")
                        .cardCategory("Category1")
                        .build(),
                FlipCard.builder()
                        .id(2L)
                        .cardTitle("카드2")
                        .cardContent("내용2")
                        .cardCategory("Category2")
                        .build(),
                FlipCard.builder()
                        .id(3L)
                        .cardTitle("카드3")
                        .cardContent("내용3")
                        .cardCategory("Category1")
                        .build(),
                FlipCard.builder()
                        .id(4L)
                        .cardTitle("카드4")
                        .cardContent("내용4")
                        .cardCategory("Category2")
                        .build(),
                FlipCard.builder()
                        .id(5L)
                        .cardTitle("카드5")
                        .cardContent("내용5")
                        .cardCategory("Category1")
                        .build(),
                FlipCard.builder()
                        .id(6L)
                        .cardTitle("카드6")
                        .cardContent("내용6")
                        .cardCategory("Category2")
                        .build(),
                FlipCard.builder()
                        .id(7L)
                        .cardTitle("카드7")
                        .cardContent("내용7")
                        .cardCategory("Category1")
                        .build(),
                FlipCard.builder()
                        .id(8L)
                        .cardTitle("카드8")
                        .cardContent("내용8")
                        .cardCategory("Category2")
                        .build()
        );

        // LimitCardGame 설정
        limitCardGame = LimitCardGame.builder()
                .member(testMember)
                .build();
    }

    @Test
    @DisplayName("난이도별 랜덤 카드 찾기 - begin(4장)")
    void findRandomCardsForBeginnerLevel() {
        // given
        given(flipCardRepository.findAll()).willReturn(testCards);

        // when
        List<FlipCardResponse> result = flipCardService.findRandomCards("begin");

        // then
        assertThat(result).hasSize(4);
        result.forEach(card -> {
            assertThat(card.cardTitle()).isNotNull();
            assertThat(card.cardContent()).isNotNull();
            assertThat(card.cardCategory()).isNotNull();
        });
    }

    @Test
    @DisplayName("난이도별 랜덤 카드 찾기 - mid(6장)")
    void findRandomCardsForMidLevel() {
        // given
        given(flipCardRepository.findAll()).willReturn(testCards);

        // when
        List<FlipCardResponse> result = flipCardService.findRandomCards("mid");

        // then
        assertThat(result).hasSize(6);
        result.forEach(card -> {
            assertThat(card.cardTitle()).isNotNull();
            assertThat(card.cardContent()).isNotNull();
            assertThat(card.cardCategory()).isNotNull();
        });
    }

    @Test
    @DisplayName("난이도별 랜덤 카드 찾기 - adv(8장)")
    void findRandomCardsForAdvancedLevel() {
        // given
        given(flipCardRepository.findAll()).willReturn(testCards);

        // when
        List<FlipCardResponse> result = flipCardService.findRandomCards("adv");

        // then
        assertThat(result).hasSize(8);
        result.forEach(card -> {
            assertThat(card.cardTitle()).isNotNull();
            assertThat(card.cardContent()).isNotNull();
            assertThat(card.cardCategory()).isNotNull();
        });
    }

    @Test
    @DisplayName("잘못된 난이도로 카드 찾기 시도시 예외 발생")
    void findRandomCardsWithInvalidDifficulty() {
        assertThatThrownBy(() -> flipCardService.findRandomCards("invalid"))
                .isInstanceOf(FlipCardDfficultyNotFoundException.class)
                .hasMessageContaining("[플립카드] 해당 난이도가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("새로운 사용자의 첫 플레이 기록 생성")
    void saveFirstPlay() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(limitCardGameRepository.findById(1L)).willReturn(Optional.empty());
        given(limitCardGameRepository.save(any(LimitCardGame.class))).willReturn(limitCardGame);

        // when
        flipCardService.saveOrUpdatePlay(1L, "begin");

        // then
        verify(limitCardGameRepository).save(any(LimitCardGame.class));
    }

    @Test
    @DisplayName("기존 사용자의 플레이 기록 업데이트")
    void updateExistingPlay() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(limitCardGameRepository.findById(1L)).willReturn(Optional.of(limitCardGame));

        // when
        flipCardService.saveOrUpdatePlay(1L, "begin");

        // then
        assertThat(limitCardGame.getBeginLastPlayed()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 플레이 시도시 예외 발생")
    void savePlayWithNonExistentMember() {
        // given
        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> flipCardService.saveOrUpdatePlay(999L, "begin"))
                .isInstanceOf(FlipCardMemberNotFoundException.class)
                .hasMessageContaining("[플립카드] 해당 회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("새로운 사용자의 플레이 가능 여부 확인")
    void checkPlayAvailableForFirstTime() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(limitCardGameRepository.findById(1L)).willReturn(Optional.empty());

        // when
        PlayFlipCardAvailable result = flipCardService.checkPlayAvailable(1L);

        // then
        assertThat(result.isBegin()).isTrue();
        assertThat(result.isMid()).isTrue();
        assertThat(result.isAdv()).isTrue();
    }

    @Test
    @DisplayName("기존 사용자의 플레이 가능 여부 확인")
    void checkPlayAvailableWithExistingPlay() {
        // given
        LimitCardGame existingGame = LimitCardGame.builder()
                .member(testMember)
                .build();

        existingGame.updateBeginLastPlayed(); // 오늘 플레이
        existingGame.updateMidLastPlayed(); // 오늘 플레이
        // advLastPlayed는 기본값(한달 전)으로 유지

        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(limitCardGameRepository.findById(1L)).willReturn(Optional.of(existingGame));

        // when
        PlayFlipCardAvailable result = flipCardService.checkPlayAvailable(1L);

        // then
        assertThat(result.isBegin()).isFalse(); // 오늘 이미 플레이했으므로 false
        assertThat(result.isMid()).isFalse(); // 오늘 이미 플레이했으므로 false
        assertThat(result.isAdv()).isTrue(); // 한달 전이 마지막 플레이이므로 true
    }
}
