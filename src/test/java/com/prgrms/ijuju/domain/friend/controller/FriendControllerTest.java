// package com.prgrms.ijuju.domain.friend.controller;

// import com.prgrms.ijuju.domain.friend.dto.request.FriendRequestDTO;
// import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
// import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
// import com.prgrms.ijuju.domain.friend.service.FriendService;
// import com.prgrms.ijuju.domain.member.entity.Member;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.ResponseEntity;

// import java.util.Collections;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class FriendControllerTest {

//     @InjectMocks
//     private FriendController friendController;

//     @Mock
//     private FriendService friendService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     @DisplayName("유저 검색")
//     void testSearchUsers() {
//         String username = "testUser";
//         UserResponseDTO userResponseDTO = new UserResponseDTO(Member.builder().id(1L).build(), true);
//         when(friendService.searchUsersByUsername(username)).thenReturn(Collections.singletonList(userResponseDTO));

//         ResponseEntity<List<UserResponseDTO>> response = friendController.searchUsers(username);

//         assertEquals(200, response.getStatusCodeValue());
//         assertEquals(1, response.getBody().size());
//     }

//     @Test
//     @DisplayName("친구 요청 보내기")
//     void testSendFriendRequest() {
//         FriendRequestDTO requestDTO = new FriendRequestDTO(1L, 2L);
//         doNothing().when(friendService).sendFriendRequest(requestDTO);

//         ResponseEntity<Void> response = friendController.sendFriendRequest(requestDTO);

//         assertEquals(200, response.getStatusCodeValue());
//     }

//     @Test
//     @DisplayName("친구 요청 수락")
//     void testAcceptFriendRequest() {
//         ResponseEntity<Void> response = friendController.acceptFriendRequest(1L);

//         assertEquals(200, response.getStatusCodeValue());
//         verify(friendService, times(1)).acceptFriendRequest(1L);
//     }

//     @Test
//     @DisplayName("친구 요청 목록 조회")
//     void testGetReceivedFriendRequests() {
//         when(friendService.getReceivedFriendRequests(2L)).thenReturn(Collections.emptyList());

//         ResponseEntity<List<FriendRequest>> response = friendController.getReceivedFriendRequests(2L);

//         assertEquals(200, response.getStatusCodeValue());
//         assertTrue(response.getBody().isEmpty());
//     }

//     @Test
//     @DisplayName("친구 목록 조회")
//     void testGetFriends() {
//         when(friendService.getFriends(1L)).thenReturn(Collections.emptyList());

//         ResponseEntity<List<UserResponseDTO>> response = friendController.getFriends(1L);

//         assertEquals(200, response.getStatusCodeValue());
//         assertTrue(response.getBody().isEmpty());
//     }
// }
