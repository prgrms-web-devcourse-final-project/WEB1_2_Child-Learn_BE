package com.prgrms.ijuju.domain.friend.service;

import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.UserRepository;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendListRepository friendListRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 전체 사용자 목록 조회 - member
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }   

    // 사용자 별명으로 검색
    public List<UserResponseDTO> searchUsersByUsername(String username) {
        List<Member> users = userRepository.findByUsernameContaining(username);
        return users.stream()
                .map(user -> new UserResponseDTO(user, 
                    friendListRepository.findByMemberId(user.getId()).stream()
                        .anyMatch(friendList -> friendList.getFriend().getId().equals(user.getId()))))
                .collect(Collectors.toList());
    }

    // 친구 요청 보내기
    public void sendFriendRequest(Long senderId, Long receiverId) {
        FriendRequest request = new FriendRequest();
        request.setSender(userRepository.findById(senderId).orElseThrow());
        request.setReceiver(userRepository.findById(receiverId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        friendRequestRepository.save(request);
    }

    // 친구 요청 수락
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // 친구 목록에 추가
        FriendList friendList = new FriendList();
        friendList.setMember(request.getReceiver());
        friendList.setFriend(request.getSender());
        friendListRepository.save(friendList);
    }

    // 친구 요청 상태 조회
    public RequestStatus getFriendRequestStatus(Long senderId, Long receiverId) {
        return friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(FriendRequest::getStatus)
                .orElse(null);
    }

    // 친구 요청 거절
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    // 받은 친구 요청 목록 조회
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
    }
}
