package com.prgrms.ijuju.domain.friend.service;

import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDto;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.UserRepository;
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

    // 전체 사용자 목록 조회

    // 사용자 별명으로 검색


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

    // 친구 삭제
    public void removeFriend(Long memberId, Long friendId) {
        friendListRepository.deleteByMemberIdAndFriendId(memberId, friendId);
    }

    // 친구 목록 조회
    public List<UserResponseDto> getFriends(Long memberId) {
        return friendListRepository.findAllByMemberIdOrFriendId(memberId, memberId).stream()
                .map(friendList -> new UserResponseDto(friendList.getFriend(), true))
                .collect(Collectors.toList());
    }
}