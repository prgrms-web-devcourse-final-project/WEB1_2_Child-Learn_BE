package com.prgrms.ijuju.domain.ranking.controller;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.ranking.repository.RankingRepository;

import com.prgrms.ijuju.global.auth.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendListRepository friendListRepository;

    @Autowired
    private RankingRepository rankingRepository;

    private Member testMember;
    private Member friendMember1;
    private Member friendMember2;
    private Member nonFriendMember;

    @BeforeEach
    void setUp() {
        // 테스트 멤버 생성
        testMember = memberRepository.save(Member.builder()
                .loginId("test")
                .pw("password")
                .username("testUser")
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .points(1000L)
                .coins(1000L)
                .isActive(true)
                .build());

        friendMember1 = memberRepository.save(Member.builder()
                .loginId("friend1")
                .pw("password")
                .username("friend1")
                .email("friend1@test.com")
                .birth(LocalDate.of(1991, 1, 1))
                .points(2000L)
                .coins(2000L)
                .isActive(true)
                .build());

        friendMember2 = memberRepository.save(Member.builder()
                .loginId("friend2")
                .pw("password")
                .username("friend2")
                .email("friend2@test.com")
                .birth(LocalDate.of(1992, 1, 1))
                .points(3000L)
                .coins(3000L)
                .isActive(true)
                .build());

        nonFriendMember = memberRepository.save(Member.builder()
                .loginId("nonFriend")
                .pw("password")
                .username("nonFriend")
                .email("nonFriend@test.com")
                .birth(LocalDate.of(1993, 1, 1))
                .points(4000L)
                .coins(4000L)
                .isActive(true)
                .build());

        // 친구 관계 설정
        List<FriendList> friendships = Arrays.asList(
                FriendList.builder().member(testMember).friend(friendMember1).build(),
                FriendList.builder().member(testMember).friend(friendMember2).build()
        );
        friendListRepository.saveAll(friendships);

        // 랭킹 데이터 생성
        LocalDateTime now = LocalDateTime.now();
        List<Ranking> rankings = Arrays.asList(
                Ranking.builder().member(testMember).weeklyPoints(100L)
                        .weekStart(now).weekEnd(now.plusDays(7)).build(),
                Ranking.builder().member(friendMember1).weeklyPoints(200L)
                        .weekStart(now).weekEnd(now.plusDays(7)).build(),
                Ranking.builder().member(friendMember2).weeklyPoints(300L)
                        .weekStart(now).weekEnd(now.plusDays(7)).build(),
                Ranking.builder().member(nonFriendMember).weeklyPoints(400L)
                        .weekStart(now).weekEnd(now.plusDays(7)).build()
        );
        rankingRepository.saveAll(rankings);
    }

    @AfterEach
    void cleanup() {
        rankingRepository.deleteAll();
        friendListRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "testUser")
    @DisplayName("전체 랭킹 목록 조회 - 친구/비친구 모두 포함")
    void showAllRankings() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/rank/week")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].weeklyPoints").value(400))
                .andExpect(jsonPath("$.content[0].username").value(nonFriendMember.getUsername()))
                .andExpect(jsonPath("$.content[1].weeklyPoints").value(300))
                .andExpect(jsonPath("$.content[2].weeklyPoints").value(200))
                .andExpect(jsonPath("$.content[3].weeklyPoints").value(100))
                .andDo(print());
    }

    @Test
    @DisplayName("친구 랭킹 목록 조회")
    void showFriendRankings() throws Exception {
        // SecurityContext 설정
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityUser securityUser = new SecurityUser(
                testMember.getId(),  // 실제 저장된 testMember의 ID 사용
                testMember.getUsername(),
                testMember.getPw(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        securityContext.setAuthentication(auth);

        mockMvc.perform(get("/api/v1/rank/friend")
                        .with(securityContext(securityContext))  // SecurityContext 적용
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser(id = 1L, username = "testUser")
    @DisplayName("비친구 사용자의 랭킹 조회")
    void findNonFriendMemberRankByUsername() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/rank")
                        .param("username", nonFriendMember.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(nonFriendMember.getUsername()))
                .andExpect(jsonPath("$.rank").value(1)) // 가장 높은 포인트
                .andExpect(jsonPath("$.weeklyPoints").value(400))
                .andDo(print());
    }
}
