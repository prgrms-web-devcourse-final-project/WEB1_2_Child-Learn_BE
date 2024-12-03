package com.prgrms.ijuju.domain.minigame.wordquiz.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.LimitWordQuiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LimitWordQuizRepositoryTest {

    @Autowired
    private LimitWordQuizRepository limitWordQuizRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private final LocalDate today = LocalDate.now();
    private List<LimitWordQuiz> savedLimitWordQuizzes;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .loginId("tester")
                .pw("tester12")
                .username("테스터")
                .birth(LocalDate.of(2000, 11, 29))
                .email("tester@example.com")
                .build();
        memberRepository.save(member);

        List<LimitWordQuiz> limitWordQuizzes = Arrays.stream(Difficulty.values())
                .map(difficulty -> LimitWordQuiz.builder()
                        .member(member)
                        .difficulty(difficulty)
                        .build())
                .toList();
        savedLimitWordQuizzes = limitWordQuizRepository.saveAll(limitWordQuizzes);
    }

    @Test
    @DisplayName("회원 가입 시 LimitWordQuiz의 lastPlayedDate는 가입일 -1일로 설정된다.")
    void createWordQuizInitLastPlayedDate() {
        // given
        LocalDate yesterday = today.minusDays(1);

        // when - @BeforeEach 에서 이미 저장

        // then
        savedLimitWordQuizzes.forEach(limitWordQuiz ->
                assertThat(limitWordQuiz.getLastPlayedDate()).isEqualTo(yesterday)
        );
    }

    @Test
    @DisplayName("member_id가 주어진 경우 해당 사용자의 LimitWordQuiz의 난이도 별 lastPlayedDate를 조회한다.")
    void readLastPlayedDateByMemberId() {
        // given - BeforeEach 설정됨
        LocalDate yesterday = today.minusDays(1);

        // when
        List<LimitWordQuiz> foundQuizzes = limitWordQuizRepository.findByMember(member);

        // then
        assertThat(foundQuizzes).hasSize(Difficulty.values().length);

        // 모든 난이도가 포함되어 있는지 확인
        assertThat(foundQuizzes)
                .extracting(LimitWordQuiz::getDifficulty)
                .containsExactlyInAnyOrder(Difficulty.values());

        foundQuizzes.forEach(limitWordQuiz -> {
            assertThat(limitWordQuiz.getMember().getId()).isEqualTo(member.getId());
            assertThat(limitWordQuiz.getLastPlayedDate()).isEqualTo(yesterday);
        });
    }

}
