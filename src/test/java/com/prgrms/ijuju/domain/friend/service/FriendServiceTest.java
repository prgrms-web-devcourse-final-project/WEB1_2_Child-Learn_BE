package com.prgrms.ijuju.domain.friend.service;

import com.prgrms.ijuju.domain.friend.dto.request.FriendRequestDTO;
import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FriendListRepository friendListRepository;

    private Member sender;
    private Member receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = Member.builder().id(1L).build();
        receiver = Member.builder().id(2L).build();
    }

    @Test
    @DisplayName("친구 요청 보내기")
    void testSendFriendRequest() {
        FriendRequestDTO requestDTO = new FriendRequestDTO(1L, 2L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(receiver));

        friendService.sendFriendRequest(requestDTO);

        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    @DisplayName("친구 요청 수락")
    void testAcceptFriendRequest() {
        Long requestId = 1L;
        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestStatus.ACCEPTED)
                .build();
        when(friendRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    
        friendService.acceptFriendRequest(requestId);
    
        // request 객체의 상태가 아닌, 새로운 객체의 상태를 확인해야 합니다.
        assertEquals(RequestStatus.ACCEPTED, request.getStatus());
        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class)); // save 메서드가 호출된 객체를 검증
        verify(friendListRepository, times(2)).save(any(FriendList.class));
    }

    @Test
    @DisplayName("친구 요청 목록 조회")
    void testGetReceivedFriendRequests() {
        when(friendRequestRepository.findByReceiverId(2L)).thenReturn(Collections.singletonList(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestStatus.PENDING)
                .build()));

        List<FriendRequest> requests = friendService.getReceivedFriendRequests(2L);

        assertEquals(1, requests.size());
    }

    @Test
    @DisplayName("친구 목록 조회")
    void testGetFriends() {
        when(friendListRepository.findByMemberId(1L)).thenReturn(Collections.emptyList());

        List<UserResponseDTO> friends = friendService.getFriends(1L);

        assertTrue(friends.isEmpty());
    }

    @Test
    @DisplayName("친구 삭제")
    void testRemoveFriend() {
        Long memberId = 1L;
        Long friendId = 2L;

        friendService.removeFriend(memberId, friendId);

        verify(friendListRepository, times(2)).deleteByMemberIdAndFriendId(anyLong(), anyLong());

    }
}
