// package com.prgrms.ijuju.domain.ranking.service;

// import com.prgrms.ijuju.domain.friend.entity.FriendList;
// import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
// import com.prgrms.ijuju.domain.member.entity.Member;
// import com.prgrms.ijuju.domain.member.repository.MemberRepository;
// import com.prgrms.ijuju.domain.point.repository.PointDetailsRepository;
// import com.prgrms.ijuju.domain.ranking.dto.response.RankingResponse;
// import com.prgrms.ijuju.domain.ranking.entity.Ranking;
// import com.prgrms.ijuju.domain.ranking.repository.RankingRepository;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.DayOfWeek;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.ArgumentMatchers.any;

// @SpringBootTest
// @ActiveProfiles("test")
// @Transactional
// class RankingServiceTest {

//     @Autowired
//     private RankingService rankingService;

//     @Autowired
//     private MemberRepository memberRepository;

//     @Autowired
//     private RankingRepository rankingRepository;

//     @Autowired
//     private FriendListRepository friendListRepository;

//     @MockBean
//     private PointDetailsRepository pointDetailsRepository;

//     private Member member1;
//     private Member member2;
//     private Member member3;
//     private LocalDateTime weekStart;
//     private LocalDateTime weekEnd;

//     @BeforeEach
//     void setUp() {
//         memberRepository.deleteAll();
//         rankingRepository.deleteAll();
//         friendListRepository.deleteAll();

//         weekStart = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.MONDAY);
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
//     @DisplayName("전체 랭킹 리스트 조회")
//     void showAllRankingList() {
//         // given
//         PageRequest pageRequest = PageRequest.of(0, 2);

//         // when
//         Page<RankingResponse> result = rankingService.showAllRankingList(pageRequest);

//         // then
//         assertThat(result.getContent()).hasSize(2);
//         assertThat(result.getContent().get(0).getWeeklyPoints()).isEqualTo(3000L);
//         assertThat(result.getContent().get(1).getWeeklyPoints()).isEqualTo(2000L);
//     }

//     @Test
//     @DisplayName("친구 랭킹 리스트 조회")
//     void showFriendRankingList() {
//         // given
//         FriendList friendList1 = FriendList.builder()
//                 .member(member1)
//                 .friend(member2)
//                 .build();
//         FriendList friendList2 = FriendList.builder()
//                 .member(member1)
//                 .friend(member3)
//                 .build();
//         friendListRepository.saveAll(List.of(friendList1, friendList2));

//         PageRequest pageRequest = PageRequest.of(0, 2);

//         // when
//         Page<RankingResponse> result = rankingService.showFriendRankingList(member1.getId(), pageRequest);

//         // then
//         assertThat(result.getContent()).hasSize(2);
//         assertThat(result.getContent().get(0).getWeeklyPoints()).isEqualTo(3000L);
//         assertThat(result.getContent().get(1).getWeeklyPoints()).isEqualTo(2000L);
//     }

//     @Test
//     @DisplayName("랭킹 업데이트")
//     void updateRanking() {
//         // given
//         Long newPoints = 5000L;

//         // Mock 설정
//         given(pointDetailsRepository.calculateEarnedPointsForMember(
//                 anyLong(),
//                 any(LocalDateTime.class),
//                 any(LocalDateTime.class)
//         )).willReturn(newPoints);

//         // when
//         rankingService.updateRanking();

//         // then
//         List<Ranking> rankings = rankingRepository.findAll();
//         assertThat(rankings).allMatch(ranking -> ranking.getWeeklyPoints() == newPoints);
//     }

//     @AfterEach
//     void cleanup() {
//         rankingRepository.deleteAll();
//         friendListRepository.deleteAll();
//         memberRepository.deleteAll();
//     }
// }
