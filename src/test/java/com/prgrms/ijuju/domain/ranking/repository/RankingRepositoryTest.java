// package com.prgrms.ijuju.domain.ranking.repository;

// import com.prgrms.ijuju.domain.member.entity.Member;
// import com.prgrms.ijuju.domain.member.repository.MemberRepository;
// import com.prgrms.ijuju.domain.ranking.entity.Ranking;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// @ActiveProfiles("test")
// @Transactional
// class RankingRepositoryTest {

//     @Autowired
//     private RankingRepository rankingRepository;

//     @Autowired
//     private MemberRepository memberRepository;

//     private Member member1;
//     private Member member2;
//     private Member member3;
//     private LocalDateTime weekStart;
//     private LocalDateTime weekEnd;

//     @BeforeEach
//     void setUp() {
//         weekStart = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
//         weekEnd = weekStart.plusDays(7);

//         // 테스트용 회원 생성
//         member1 = memberRepository.save(Member.builder()
//                 .loginId("test1")
//                 .pw("password")
//                 .username("user1")
//                 .email("test1@test.com")
//                 .birth(LocalDate.of(2000, 1, 1))
//                 .isActive(true)
//                 .build());

//         member2 = memberRepository.save(Member.builder()
//                 .loginId("test2")
//                 .pw("password")
//                 .username("user2")
//                 .email("test2@test.com")
//                 .birth(LocalDate.of(2000, 1, 1))
//                 .isActive(true)
//                 .build());

//         member3 = memberRepository.save(Member.builder()
//                 .loginId("test3")
//                 .pw("password")
//                 .username("user3")
//                 .email("test3@test.com")
//                 .birth(LocalDate.of(2000, 1, 1))
//                 .isActive(true)
//                 .build());

//         // 랭킹 데이터 생성
//         rankingRepository.save(Ranking.builder()
//                 .member(member1)
//                 .weekStart(weekStart)
//                 .weekEnd(weekEnd)
//                 .weeklyPoints(1000L)
//                 .build());

//         rankingRepository.save(Ranking.builder()
//                 .member(member2)
//                 .weekStart(weekStart)
//                 .weekEnd(weekEnd)
//                 .weeklyPoints(2000L)
//                 .build());

//         rankingRepository.save(Ranking.builder()
//                 .member(member3)
//                 .weekStart(weekStart)
//                 .weekEnd(weekEnd)
//                 .weeklyPoints(3000L)
//                 .build());
//     }

//     @Test
//     @DisplayName("회원 ID로 랭킹엔티티 포인트 조회")
//     void findByMemberId() {
//         Optional<Ranking> ranking = rankingRepository.findByMemberId(member1.getId());

//         assertThat(ranking).isPresent();
//         assertThat(ranking.get().getWeeklyPoints()).isEqualTo(1000L);
//     }

//     @Test
//     @DisplayName("주간 포인트 기준 내림차순 정렬 페이징 조회")
//     void findAllByOrderByWeeklyPointsDesc() {
//         PageRequest pageRequest = PageRequest.of(0, 2);
//         Page<Ranking> rankings = rankingRepository.findAllByOrderByWeeklyPointsDesc(pageRequest);

//         assertThat(rankings.getContent()).hasSize(2);
//         assertThat(rankings.getContent().get(0).getWeeklyPoints()).isEqualTo(3000L);
//         assertThat(rankings.getContent().get(1).getWeeklyPoints()).isEqualTo(2000L);
//     }

//     @Test
//     @DisplayName("특정 회원의 순위 조회")
//     void findRankByMemberId() {
//         Long rank = rankingRepository.findRankByMemberId(member1.getId());

//         assertThat(rank).isEqualTo(3L); // 1000포인트로 3등
//     }

//     @Test
//     @DisplayName("주간 시작/종료 시간 업데이트")
//     void updateWeekStartAndEnd() {
//         LocalDateTime newWeekStart = weekStart.plusDays(7);
//         LocalDateTime newWeekEnd = weekEnd.plusDays(7);

//         rankingRepository.updateWeekStartAndEnd(newWeekStart, newWeekEnd);

//         List<Ranking> rankings = rankingRepository.findAll();
//         assertThat(rankings)
//                 .allMatch(ranking ->
//                         ranking.getWeekStart().equals(newWeekStart) &&
//                                 ranking.getWeekEnd().equals(newWeekEnd)
//                 );
//     }

//     @AfterEach
//     void cleanup() {
//         rankingRepository.deleteAll();
//         memberRepository.deleteAll();
//     }
// }
