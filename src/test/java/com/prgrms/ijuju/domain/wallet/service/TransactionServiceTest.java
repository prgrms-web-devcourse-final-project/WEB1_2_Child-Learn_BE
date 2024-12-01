// package com.prgrms.ijuju.domain.wallet.service;

// import static org.assertj.core.api.Assertions.assertThat;   
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.verify;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.prgrms.ijuju.domain.member.entity.Member;
// import com.prgrms.ijuju.domain.wallet.entity.PointTransaction;
// import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
// import com.prgrms.ijuju.domain.wallet.entity.PointType;
// import com.prgrms.ijuju.domain.wallet.entity.Wallet;
// import com.prgrms.ijuju.domain.wallet.repository.PointTransactionRepository;
// import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
// import com.prgrms.ijuju.global.websocket.WebSocketHandler;
// import com.prgrms.ijuju.domain.wallet.dto.response.PointTransactionResponseDTO;

// @ExtendWith(MockitoExtension.class)
// class TransactionServiceTest {

//     @InjectMocks
//     private TransactionService transactionService;

//     @Mock
//     private PointTransactionRepository pointTransactionRepository;
    
//     @Mock
//     private WalletRepository walletRepository;
    
//     @Mock
//     private WebSocketHandler webSocketHandler;

//     private Member testMember;
//     private Wallet testWallet;
//     private PointTransaction testTransaction;

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

//         testTransaction = PointTransaction.builder()
//                 .id(1L)
//                 .member(testMember)
//                 .transactionType(TransactionType.EARNED)
//                 .points(100L)
//                 .pointType(PointType.ATTENDANCE)
//                 .build();
//     }

//     @Test
//     @DisplayName("거래 내역 저장 성공")
//     void saveTransaction_Success() {
//         given(walletRepository.findByMemberId(anyLong()))
//                 .willReturn(Optional.of(testWallet));

//         transactionService.saveTransaction(
//             testMember, TransactionType.EARNED, 
//             100L, PointType.ATTENDANCE, null
//         );

//         verify(pointTransactionRepository).save(any(PointTransaction.class));
//     }

//     @Test
//     @DisplayName("거래 내역 조회 성공")
//     void getTransactionHistory_Success() {
//         List<PointTransaction> transactions = List.of(testTransaction);
//         given(pointTransactionRepository.findByMemberIdOrderByCreatedAtDesc(anyLong()))
//                 .willReturn(transactions);

//         List<PointTransactionResponseDTO> response = 
//             transactionService.getTransactionHistory(1L);

//         assertThat(response).hasSize(1);
//         assertThat(response.get(0).getPoints()).isEqualTo(100L);
//     }

//     @Test
//     @DisplayName("거래 유형별 내역 조회 성공")
//     void getTransactionsByType_Success() {
//         List<PointTransaction> transactions = List.of(testTransaction);
//         given(pointTransactionRepository
//                 .findByMemberIdAndTransactionTypeOrderByCreatedAtDesc(
//                     anyLong(), any(TransactionType.class)))
//                 .willReturn(transactions);

//         List<PointTransactionResponseDTO> response = 
//             transactionService.getTransactionsByType(1L, TransactionType.EARNED);

//         assertThat(response).hasSize(1);
//         assertThat(response.get(0).getTransactionType())
//                 .isEqualTo(TransactionType.EARNED.toString());
//     }
// }
