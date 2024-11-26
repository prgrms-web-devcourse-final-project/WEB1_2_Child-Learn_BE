package com.prgrms.ijuju.domain.friend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.UserRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

    @Mock
    private FriendListRepository friendListRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FriendService friendService;

    @Test
    @DisplayName("전체 회원 목록을 조회")
    void getAllMembersTest() {
        List<Member> expectedMembers = Arrays.asList(
            Member.builder().id(1L).username("user1").build(),
            Member.builder().id(2L).username("user2").build()
        );
        when(memberRepository.findAll()).thenReturn(expectedMembers);

        List<Member> actualMembers = friendService.getAllMembers();

        assertThat(actualMembers).hasSize(2);
        verify(memberRepository).findAll();
    }

    @Test
    @DisplayName("사용자 검색")
    void searchUsersByUsernameTest() {
        String searchKeyword = "user";
        Member member = Member.builder()
            .id(1L)
            .username("user1")
            .build();
        List<Member> members = Collections.singletonList(member);
        
        when(userRepository.findByUsernameContaining(searchKeyword)).thenReturn(members);
        when(friendListRepository.findByMemberId(anyLong())).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = friendService.searchUsersByUsername(searchKeyword);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("친구 요청")
    void sendFriendRequestTest() {
        Long senderId = 1L;
        Long receiverId = 2L;
        Member sender = Member.builder().id(senderId).build();
        Member receiver = Member.builder().id(receiverId).build();

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        friendService.sendFriendRequest(senderId, receiverId);

        verify(friendRequestRepository).save(any(FriendRequest.class));
    }

    @Test
    @DisplayName("친구 요청 수락")
    void acceptFriendRequestTest() {
        Long requestId = 1L;
        FriendRequest request = new FriendRequest();
        request.setSender(Member.builder().id(1L).build());
        request.setReceiver(Member.builder().id(2L).build());

        when(friendRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        friendService.acceptFriendRequest(requestId);

        assertThat(request.getStatus()).isEqualTo(RequestStatus.ACCEPTED);
        verify(friendRequestRepository).save(request);
        verify(friendListRepository).save(any(FriendList.class));
    }

    @Test
    @DisplayName("친구 요청 거절")
    void rejectFriendRequestTest() {
        Long requestId = 1L;
        FriendRequest request = new FriendRequest();
        when(friendRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        friendService.rejectFriendRequest(requestId);

        assertThat(request.getStatus()).isEqualTo(RequestStatus.REJECTED);
        verify(friendRequestRepository).save(request);
    }

    @Test
    @DisplayName("받은 친구 요청 목록을 조회")
    void getReceivedFriendRequestsTest() {
        Long receiverId = 1L;
        List<FriendRequest> expectedRequests = Arrays.asList(
            new FriendRequest(),
            new FriendRequest()
        );

        when(friendRequestRepository.findByReceiverId(receiverId))
            .thenReturn(expectedRequests);

        List<FriendRequest> actualRequests = friendService.getReceivedFriendRequests(receiverId);

        assertThat(actualRequests).hasSize(2);
    }

    @Test
    @DisplayName("친구 목록을 조회")
    void getFriendsTest() {
        Long memberId = 1L;
        Member friend = Member.builder()
            .id(2L)
            .username("friend")
            .build();
        FriendList friendList = FriendList.builder()
            .member(Member.builder().id(memberId).build())
            .friend(friend)
            .build();
        
        when(friendListRepository.findByMemberId(memberId))
            .thenReturn(Collections.singletonList(friendList));

        List<UserResponseDTO> result = friendService.getFriends(memberId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("friend");
    }

    @Test
    @DisplayName("친구 삭제")
    void removeFriendTest() {
        Long memberId = 1L;
        Long friendId = 2L;

        friendService.removeFriend(memberId, friendId);

        verify(friendListRepository).deleteByMemberIdAndFriendId(memberId, friendId);
    }
} 