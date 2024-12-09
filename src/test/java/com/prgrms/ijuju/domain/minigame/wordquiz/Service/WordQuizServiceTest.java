// package com.prgrms.ijuju.domain.minigame.wordquiz.Service;

// import com.prgrms.ijuju.domain.member.entity.Member;
// import com.prgrms.ijuju.domain.member.repository.MemberRepository;
// import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizAvailabilityResponse;
// import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizResponse;
// import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
// import com.prgrms.ijuju.domain.minigame.wordquiz.entity.LimitWordQuiz;
// import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
// import com.prgrms.ijuju.domain.minigame.wordquiz.repository.LimitWordQuizRepository;
// import com.prgrms.ijuju.domain.minigame.wordquiz.repository.WordQuizRepository;
// import com.prgrms.ijuju.domain.minigame.wordquiz.service.WordQuizService;
// import jakarta.servlet.http.HttpSession;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class WordQuizServiceTest {

//     @Mock
//     private LimitWordQuizRepository limitWordQuizRepository;

//     @Mock
//     private WordQuizRepository wordQuizRepository;

//     @Mock
//     private MemberRepository memberRepository;

//     @Mock
//     private HttpSession session;

//     @InjectMocks
//     private WordQuizService wordQuizService;

//     private Member member;
//     private WordQuiz wordQuiz;

//     @BeforeEach
//     void setUp() {
//         member = Member.builder()
//                 .loginId("tester")
//                 .pw("tester12")
//                 .username("테스터")
//                 .birth(LocalDate.of(2000, 11, 29))
//                 .email("tester@example.com")
//                 .build();

//         wordQuiz = WordQuiz.builder()
//                 .word("테스트 단어")
//                 .explanation("테스트 단어의 설명")
//                 .hint("테스트 힌트입니다.")
//                 .build();
//     }

//     @Test
//     @DisplayName("회원이 당일 낱말 게임을 플레이하지 않았다면 모든 난이도가 true를 반환한다")
//     void returnAllTrue_whenNothingPlayedToday() {
//         // given - 어제 플레이한 기록만 있는 상황 설정
//         List<LimitWordQuiz> yesterdayQuizzes = createQuizListWithDate(member, LocalDate.now().minusDays(1));

//         when(memberRepository.findById(anyLong()))
//                 .thenReturn(Optional.of(member));

//         when(limitWordQuizRepository.findByMember(member))
//                 .thenReturn(yesterdayQuizzes);

//         // when
//         WordQuizAvailabilityResponse response = wordQuizService.checkPlayAvailability(1L);

//         // then
//         assertThat(yesterdayQuizzes).allMatch(
//                 quiz -> quiz.getLastPlayedDate().equals(LocalDate.now().minusDays(1)));
//         assertThat(response.isEasyPlayAvailable()).isTrue();
//         assertThat(response.isNormalPlayAvailable()).isTrue();
//         assertThat(response.isHardPlayAvailable()).isTrue();
//     }

//     @Test
//     @DisplayName("오늘 쉬움 난이도만 플레이한 경우 EASY만 false를 반환한다")
//     void returnOnlyEasyFalse_whenOnlyEasyPlayedToday() {
//         // given
//         LocalDate today = LocalDate.now();
//         List<LimitWordQuiz> quizzes = Arrays.asList(
//                 createQuizWithDate(member, Difficulty.EASY, today),
//                 createQuizWithDate(member, Difficulty.NORMAL, today.minusDays(1)),
//                 createQuizWithDate(member, Difficulty.HARD, today.minusDays(1))
//         );

//         when(memberRepository.findById(anyLong()))
//                 .thenReturn(Optional.of(member));

//         when(limitWordQuizRepository.findByMember(member))
//                 .thenReturn(quizzes);

//         // when
//         WordQuizAvailabilityResponse response = wordQuizService.checkPlayAvailability(1L);

//         // then
//         assertThat(response.isEasyPlayAvailable()).isFalse();
//         assertThat(response.isNormalPlayAvailable()).isTrue();
//         assertThat(response.isHardPlayAvailable()).isTrue();
//     }

//     @Test
//     @DisplayName("새로운 게임 시작 시 세션에 게임 상태가 저장된다")
//     void returnInitialGameState_whenStartNewGame() {
//         // given
//         when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
//         when(limitWordQuizRepository.findByMember(any())).thenReturn(List.of());
//         when(wordQuizRepository.findRandomWord()).thenReturn(Optional.of(wordQuiz));
//         when(session.getAttribute("gameState")).thenReturn(null);  // 세션에 게임 상태 없음

//         // when
//         WordQuizResponse response = wordQuizService.startOrContinueWordQuiz(session, 1L, Difficulty.EASY);

//         // then
//         assertThat(response.word()).isEqualTo(wordQuiz.getWord());
//         assertThat(response.explanation()).isEqualTo(wordQuiz.getExplanation());
//         assertThat(response.currentPhase()).isEqualTo(1);
//         assertThat(response.remainLife()).isEqualTo(3);

//         verify(session).setAttribute(eq("memberId"), eq(1L));
//         verify(session).setAttribute(eq("gameState"), any(WordQuizResponse.class));
//     }

//     @Test
//     @DisplayName("진행 중인 게임이 있으면 세션에서 상태를 불러온다")
//     void returnExistingGameState_whenGameExists() {
//         // given
//         WordQuizResponse existingState = new WordQuizResponse(
//                 "기존단어", "설명", "힌트", 2, 3, Difficulty.EASY);

//         when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
//         when(limitWordQuizRepository.findByMember(any())).thenReturn(List.of());
//         when(session.getAttribute("gameState")).thenReturn(existingState);

//         // when
//         WordQuizResponse response = wordQuizService.startOrContinueWordQuiz(session, 1L, Difficulty.EASY);

//         // then
//         assertThat(response).isEqualTo(existingState);
//         verify(wordQuizRepository, never()).findRandomWord();  // 새 단어를 가져오지 않음
//     }

//     @Test
//     @DisplayName("정답 제출 시 다음 페이즈로 넘어간다.")
//     void returnNextPhase_whenAnswerCorrect() {
//         // given
//         Long memberId = 1L;
//         WordQuizResponse gameState = new WordQuizResponse(
//                 "단어", "설명", "힌트", 1, 3, Difficulty.EASY);

//         when(session.getAttribute("memberId")).thenReturn(memberId);
//         when(session.getAttribute("gameState")).thenReturn(gameState);

//         // when
//         WordQuizResponse response = wordQuizService.handleAnswer(memberId, session, true);

//         // then
//         assertThat(response.currentPhase()).isEqualTo(2);
//         assertThat(response.remainLife()).isEqualTo(3);
//         verify(session).setAttribute(eq("gameState"), any(WordQuizResponse.class));
//     }

//     @Test
//     @DisplayName("오답 제출 시 생명이 감소한다")
//     void returnDecreasedLife_whenAnswerWrong() {
//         // given
//         Long memberId = 1L;
//         WordQuizResponse gameState = new WordQuizResponse(
//                 "단어", "설명", "힌트", 1, 3, Difficulty.EASY);

//         when(session.getAttribute("memberId")).thenReturn(memberId);
//         when(session.getAttribute("gameState")).thenReturn(gameState);

//         // when
//         WordQuizResponse response = wordQuizService.handleAnswer(memberId, session, false);

//         // then
//         assertThat(response.currentPhase()).isEqualTo(1);
//         assertThat(response.remainLife()).isEqualTo(2);
//         verify(session).setAttribute(eq("gameState"), any(WordQuizResponse.class));
//     }


//     @Test
//     @DisplayName("마지막 단계에서 정답을 맞추면 게임이 종료되며, 해당 사용자의 lastPlayedDate가 오늘로 갱신된다.")
//     void updateLastPlayedDate_whenGameCompleted() {
//         // given
//         Long memberId = 1L;
//         WordQuizResponse gameState = new WordQuizResponse(
//                 "단어", "설명", "힌트", 3, 3, Difficulty.EASY);

//         LimitWordQuiz limitWordQuiz = Mockito.mock(LimitWordQuiz.class);

//         when(session.getAttribute("memberId")).thenReturn(memberId);
//         when(session.getAttribute("gameState")).thenReturn(gameState);
//         when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//         when(limitWordQuizRepository.findByMemberAndDifficulty(member, Difficulty.EASY))
//                 .thenReturn(Optional.of(limitWordQuiz));

//         // when
//         WordQuizResponse response = wordQuizService.handleAnswer(memberId, session, true);

//         // then
//         assertThat(response.currentPhase()).isEqualTo(3);
//         verify(limitWordQuiz).changeLastPlayedDate(LocalDate.now());
//         verify(limitWordQuizRepository).save(limitWordQuiz);
//     }

//     private LimitWordQuiz createQuizWithDate(Member member, Difficulty difficulty, LocalDate date) {
//         return LimitWordQuiz.builder()
//                 .member(member)
//                 .difficulty(difficulty)
//                 .lastPlayedDate(date)
//                 .build();
//     }

//     private List<LimitWordQuiz> createQuizListWithDate(Member member, LocalDate date) {
//         return Arrays.stream(Difficulty.values())
//                 .map(difficulty -> createQuizWithDate(member, difficulty, date))
//                 .toList();
//     }
// }
