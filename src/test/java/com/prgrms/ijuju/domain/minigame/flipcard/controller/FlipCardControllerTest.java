//package com.prgrms.ijuju.domain.minigame.flipcard.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.prgrms.ijuju.domain.member.entity.Member;
//import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
//import com.prgrms.ijuju.domain.minigame.flipcard.entity.LimitCardGame;
//import com.prgrms.ijuju.domain.member.repository.MemberRepository;
//import com.prgrms.ijuju.domain.minigame.flipcard.repository.FlipCardRepository;
//import com.prgrms.ijuju.domain.minigame.flipcard.repository.LimitCardGameRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//@ActiveProfiles("test")
//class FlipCardControllerTest {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private FlipCardRepository flipCardRepository;
//
//    @Autowired
//    private LimitCardGameRepository limitCardGameRepository;
//
//    private static final String BASE_URL = "/api/v1/flip-card";
//    private Member testMember;
//    private LimitCardGame limitCardGame;
//    private List<FlipCard> testCards;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(SecurityMockMvcConfigurers.springSecurity())
//                .build();
//
//        // 테스트 멤버 생성
//        testMember = memberRepository.save(Member.builder()
//                .loginId("testUser")
//                .pw("password123")
//                .username("TestUser")
//                .email("test@test.com")
//                .birth(LocalDate.of(1990, 1, 1))
//                .build());
//
//        // 플립 카드 게임 제한 정보 생성
//        limitCardGame = limitCardGameRepository.save(LimitCardGame.builder()
//                .member(testMember)
//                .build());
//
//        // 테스트용 플립 카드 데이터 생성
//        FlipCard beginCard = FlipCard.builder()
//                .cardTitle("Begin Card")
//                .cardContent("Begin Content")
//                .cardCategory("BEGIN")
//                .build();
//        FlipCard midCard = FlipCard.builder()
//                .cardTitle("Mid Card")
//                .cardContent("Mid Content")
//                .cardCategory("MID")
//                .build();
//        FlipCard advCard = FlipCard.builder()
//                .cardTitle("Adv Card")
//                .cardContent("Adv Content")
//                .cardCategory("ADV")
//                .build();
//
//        testCards = flipCardRepository.saveAll(List.of(beginCard, midCard, advCard));
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("난이도별 랜덤 카드를 정상적으로 조회할 수 있다")
//    void showRandomCards_Success() throws Exception {
//        // given
//        String difficulty = "BEGIN";
//
//        // when & then
//        mockMvc.perform(get(BASE_URL)
//                        .param("difficulty", difficulty)
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].cardTitle").exists())
//                .andExpect(jsonPath("$[0].cardContent").exists());
//    }
//
////    @Test
////    @WithMockUser
////    @DisplayName("플립카드 게임 플레이 가능 여부를 확인할 수 있다")
////    void checkPlayAvailable_Success() throws Exception {
////        // when & then
////        mockMvc.perform(get(BASE_URL + "/available")
////                        .with(csrf()))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.isBegin").exists())
////                .andExpect(jsonPath("$.isMid").exists())
////                .andExpect(jsonPath("$.isAdv").exists());
////    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("플레이 시간을 정상적으로 업데이트할 수 있다 - BEGIN 난이도")
//    void updatePlayTime_Begin_Success() throws Exception {
//        // when & then
//        mockMvc.perform(put(BASE_URL + "/{memberId}", testMember.getId())
//                        .param("difficulty", "BEGIN")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("플레이 시간을 정상적으로 업데이트할 수 있다 - MID 난이도")
//    void updatePlayTime_Mid_Success() throws Exception {
//        // when & then
//        mockMvc.perform(put(BASE_URL + "/{memberId}", testMember.getId())
//                        .param("difficulty", "MID")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser
//    @DisplayName("플레이 시간을 정상적으로 업데이트할 수 있다 - ADV 난이도")
//    void updatePlayTime_Adv_Success() throws Exception {
//        // when & then
//        mockMvc.perform(put(BASE_URL + "/{memberId}", testMember.getId())
//                        .param("difficulty", "ADV")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//    }
//
////    @Test
////    @WithMockUser
////    @DisplayName("잘못된 난이도로 카드를 조회할 경우 실패한다")
////    void showRandomCards_WithInvalidDifficulty_Fail() throws Exception {
////        // when & then
////        mockMvc.perform(get(BASE_URL)
////                        .param("difficulty", "INVALID")
////                        .with(csrf()))
////                .andDo(print())
////                .andExpect(status().isBadRequest());
////    }
//
////    @Test
////    @WithMockUser
////    @DisplayName("존재하지 않는 회원 ID로 플레이 시간을 업데이트할 경우 실패한다")
////    void updatePlayTime_WithInvalidMemberId_Fail() throws Exception {
////        // given
////        Long invalidMemberId = 999999L;
////
////        // when & then
////        mockMvc.perform(put(BASE_URL + "/{memberId}", invalidMemberId)
////                        .param("difficulty", "BEGIN")
////                        .with(csrf()))
////                .andDo(print())
////                .andExpect(status().isNotFound());
////    }
//}