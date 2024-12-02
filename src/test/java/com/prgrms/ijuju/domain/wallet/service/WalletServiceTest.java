// package com.prgrms.ijuju.domain.wallet.service;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.verify;

// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.prgrms.ijuju.domain.member.entity.Member;
// import com.prgrms.ijuju.domain.wallet.entity.Wallet;
// import com.prgrms.ijuju.domain.wallet.entity.PointTransaction;
// import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
// import com.prgrms.ijuju.domain.wallet.entity.PointType;
// import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
// import com.prgrms.ijuju.domain.wallet.repository.PointTransactionRepository;
// import com.prgrms.ijuju.global.common.exception.CustomException;
// import com.prgrms.ijuju.domain.wallet.dto.request.ExchangeRequestDTO;
// import com.prgrms.ijuju.domain.wallet.dto.request.GamePointRequestDTO;
// import com.prgrms.ijuju.domain.wallet.dto.response.WalletResponseDTO;
// import com.prgrms.ijuju.global.websocket.WebSocketHandler;
// import com.prgrms.ijuju.domain.wallet.entity.GameType;

// @ExtendWith(MockitoExtension.class)
// class WalletServiceTest {

//     @InjectMocks
//     private WalletService walletService;

//     @Mock
//     private WalletRepository walletRepository;
    
//     @Mock
//     private PointTransactionRepository pointTransactionRepository;
    
//     @Mock
//     private WebSocketHandler webSocketHandler;

//     private Member testMember;
//     private Wallet testWallet;

//     @BeforeEach
//     void setUp() {
//         testMember = Member.builder()
//                 .id(1L)
//                 .loginId("test")
//                 .build();

//         testWallet = Wallet.builder()
//                 .id(1L)
//                 .member(testMember)
//                 .currentPoints(1000L)
//                 .currentCoins(10L)
//                 .build();
//     }

//     @Test
//     @DisplayName("현재 잔액 조회 성공")
//     void getCurrentBalance_Success() {
//         given(walletRepository.findByMemberId(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         WalletResponseDTO response = walletService.showCurrentBalance(1L);

//         assertThat(response.getCurrentPoints()).isEqualTo(1000L);
//         assertThat(response.getCurrentCoins()).isEqualTo(10L);
//     }

//     @Test
//     @DisplayName("현재 잔액 조회 실패 - 지갑 없음")
//     void getCurrentBalance_Fail_WalletNotFound() {
//         given(walletRepository.findByMemberId(anyLong()))
//                 .willReturn(Optional.empty());

//         assertThatThrownBy(() -> walletService.showCurrentBalance(1L))
//                 .isInstanceOf(CustomException.class)
//                 .hasMessageContaining("지갑을 찾을 수 없습니다");
//     }

//     @Test
//     @DisplayName("포인트 환전 성공")
//     void exchangePointsToCoin_Success() {
//         ExchangeRequestDTO request = new ExchangeRequestDTO(1L, 100L);
//         given(walletRepository.findByMemberIdWithLock(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         WalletResponseDTO response = walletService.exchangePointsToCoin(request);

//         assertThat(response.getCurrentPoints()).isEqualTo(900L);
//         assertThat(response.getCurrentCoins()).isEqualTo(11L);
//     }

//     @Test
//     @DisplayName("포인트 환전 실패 - 최소 금액 미달")
//     void exchangePointsToCoin_Fail_MinimumAmount() {
//         ExchangeRequestDTO request = new ExchangeRequestDTO(1L, 50L);
//         given(walletRepository.findByMemberIdWithLock(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         assertThatThrownBy(() -> walletService.exchangePointsToCoin(request))
//                 .isInstanceOf(CustomException.class)
//                 .hasMessageContaining("환전은 100포인트 단위로만 가능합니다");
//     }

//     @Test
//     @DisplayName("출석 체크 포인트 지급 성공")
//     void processAttendancePoints_Success() {
//         given(walletRepository.findByMemberIdWithLock(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         WalletResponseDTO response = walletService.processAttendancePoints(1L);

//         assertThat(response.getCurrentPoints()).isEqualTo(1100L);
//         verify(pointTransactionRepository).save(any(PointTransaction.class));
//     }

//     @Test
//     @DisplayName("미니게임 포인트 지급 성공")
//     void processMiniGamePoints_Success() {
//         GamePointRequestDTO request = new GamePointRequestDTO(
//             1L, TransactionType.EARNED, 50L, 
//             PointType.GAME, GameType.WORD_QUIZ, true
//         );
//         given(walletRepository.findByMemberIdWithLock(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         WalletResponseDTO response = walletService.processMiniGamePoints(request);

//         assertThat(response.getCurrentPoints()).isEqualTo(1050L);
//         verify(pointTransactionRepository).save(any(PointTransaction.class));
//     }
// }
