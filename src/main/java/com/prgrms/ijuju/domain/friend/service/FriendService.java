package com.prgrms.ijuju.domain.friend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prgrms.ijuju.domain.friend.dto.request.FriendRequestDTO;
import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.exception.FriendException;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.UserRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;

@Service
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendListRepository friendListRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 유저 검색
    public List<UserResponseDTO> searchUsersByUsername(String username) {
        List<Member> users = userRepository.findByUsernameContaining(username);
        return users.stream()
                .map(user -> new UserResponseDTO(user, 
                    friendRequestRepository.findByReceiverId(user.getId()).stream()
                        .anyMatch(friendRequest -> friendRequest.getSender().getId().equals(user.getId()))))
                .collect(Collectors.toList());
    }

    // 친구 요청 보내기
    public void sendFriendRequest(FriendRequestDTO friendRequestDto) {
        Member sender = memberRepository.findById(friendRequestDto.getSenderId())
                .orElseThrow(() -> FriendException.FRIEND_NOT_FOUND.getFriendTaskException());
        Member receiver = memberRepository.findById(friendRequestDto.getReceiverId())
                .orElseThrow(() -> FriendException.FRIEND_NOT_FOUND.getFriendTaskException());

        if (friendRequestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()).isPresent()) {
            throw FriendException.FRIEND_REQUEST_ALREADY_SENT.getFriendTaskException();
        }

        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(RequestStatus.PENDING)
                .build();

        friendRequestRepository.save(request);
    }

    // 친구 요청 수락
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> FriendException.FRIEND_REQUEST_NOT_FOUND.getFriendTaskException());
        request = request.withStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        friendListRepository.save(FriendList.builder()
                .member(request.getReceiver())
                .friend(request.getSender())
                .build());

        friendListRepository.save(FriendList.builder()
                .member(request.getSender())
                .friend(request.getReceiver())
                .build());
    }

    // // 친구 요청 상태 조회
    // public RequestStatus getFriendRequestStatus(Long senderId, Long receiverId) {
    //     return friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)
    //             .map(FriendRequest::getStatus)
    //             .orElse(null);
    // }

    // 친구 요청 거절
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> FriendException.FRIEND_REQUEST_NOT_FOUND.getFriendTaskException());
        request = request.withStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    // 친구 요청 목록 조회
    public List<FriendRequest> getReceivedFriendRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverId(receiverId);
    }

    // 친구 목록 조회
    public List<UserResponseDTO> getFriends(Long memberId) {
        return friendListRepository.findByMemberId(memberId).stream()
                .map(friendList -> new UserResponseDTO(friendList.getFriend(), true))
                .collect(Collectors.toList());
    }

    // 친구 삭제
    public void removeFriend(Long memberId, Long friendId) {
        friendListRepository.deleteByMemberIdAndFriendId(memberId, friendId);
        friendListRepository.deleteByMemberIdAndFriendId(friendId, memberId);
    }
}
