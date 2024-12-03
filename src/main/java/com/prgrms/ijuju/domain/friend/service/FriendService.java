package com.prgrms.ijuju.domain.friend.service;

import com.prgrms.ijuju.domain.friend.dto.response.FriendListResponseDTO;
import com.prgrms.ijuju.domain.friend.dto.response.FriendResponseDTO;
import com.prgrms.ijuju.domain.friend.exception.FriendException;
import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendRequestRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.friend.entity.FriendshipStatus;
import com.prgrms.ijuju.global.exception.CustomException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    
    private final FriendRequestRepository friendRequestRepository;
    private final FriendListRepository friendListRepository;
    private final MemberRepository memberRepository;    
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 친구 요청 보내기
    @Transactional
    public String sendFriendRequest(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_NOT_FOUND.getMessage()));
                
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_NOT_FOUND.getMessage()));
        
        validateFriendRequest(sender.getId(), receiver.getId());

        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .requestStatus(RequestStatus.PENDING)
                .isRead(false)
                .build();

        friendRequestRepository.save(request);
        log.info("친구 요청 생성 완료 - 발신자: {}, 수신자: {}", sender.getId(), receiver.getId());

        simpMessagingTemplate.convertAndSend("/topic/friend-requests/" + receiverId, 
            "친구 요청이 도착했습니다: " + sender.getUsername());
        
        return "친구 요청이 성공적으로 보내졌습니다.";
    }
    
    // 보낸 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public List<FriendResponseDTO> showSentFriendRequests(Long senderId) {
        return friendRequestRepository.findBySenderIdAndRequestStatus(senderId, RequestStatus.PENDING)
                .stream()
                .map(FriendResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 보낸 친구 요청 취소
    @Transactional
    public String cancelFriendRequest(Long senderId, Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_REQUEST_NOT_FOUND.getMessage()));
                
        if (!request.getSender().getId().equals(senderId)) {
            throw new CustomException(FriendException.FRIEND_REQUEST_NOT_AUTHORIZED.getMessage());
        }
        
        // 이미 처리된 요청인지 확인
        if (request.getRequestStatus() != RequestStatus.PENDING) {
            throw new CustomException(FriendException.REQUEST_ALREADY_PROCESSED.getMessage());
        }
        
        friendRequestRepository.delete(request);
        return "친구 요청이 취소되었습니다.";
    }

    // 받은 친구 요청 목록 조회
    public List<FriendResponseDTO> showReceivedFriendRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverIdAndRequestStatus(receiverId, RequestStatus.PENDING)
                .stream()
                .map(FriendResponseDTO::new)
                .collect(Collectors.toList());
    }
    // 친구 요청 읽음 표시
    @Transactional
    public void markRequestAsRead(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
            .orElseThrow(() -> new CustomException(FriendException.FRIEND_REQUEST_NOT_FOUND.getMessage()));
        request.markAsRead();
        friendRequestRepository.save(request);
    }

    // 친구 요청 수락 (친구 등록)
    @Transactional
    public String acceptFriendRequest(Long requestId, Long receiverId) {
        try {
            FriendRequest request = friendRequestRepository.findById(requestId)
                    .orElseThrow(() -> new CustomException(FriendException.FRIEND_REQUEST_NOT_FOUND.getMessage()));
            
            // 수신자 검증
            if (!request.getReceiver().getId().equals(receiverId)) {
                throw new CustomException(FriendException.FRIEND_REQUEST_NOT_AUTHORIZED.getMessage());
            }
            
            // 상태 검증
            if (request.getRequestStatus() != RequestStatus.PENDING) {
                throw new CustomException(FriendException.REQUEST_ALREADY_PROCESSED.getMessage());
            }
            
            request.accept();
            createMutualFriendship(request.getSender(), request.getReceiver());
            
            // 친구 요청 수락 알림 추가
            simpMessagingTemplate.convertAndSend(
                "/topic/friend-requests/" + request.getSender().getId(), 
                request.getReceiver().getUsername() + "님이 친구 요청을 수락했습니다."
            );
            
            return "친구 요청이 수락되었습니다.";
        } catch (Exception e) {
            log.error("친구 요청 수락 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    // 친구 요청 거절
    @Transactional
    public String rejectFriendRequest(Long requestId, Long receiverId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_REQUEST_NOT_FOUND.getMessage()));
        
        // 수신자 검증
        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new CustomException(FriendException.FRIEND_REQUEST_NOT_AUTHORIZED.getMessage());
        }
        
        request.reject();
        friendRequestRepository.save(request);
        
        // 친구 요청 거절 알림 추가
        simpMessagingTemplate.convertAndSend(
            "/topic/friend-requests/" + request.getSender().getId(), 
            request.getReceiver().getUsername() + "님이 친구 요청을 거절했습니다."
        );
        
        return "친구 요청이 거절되었습니다.";
    }
    
    // 친구 목록 조회
    @Transactional(readOnly = true)
    public Page<FriendListResponseDTO> showFriends(Long memberId, Integer page, Integer size) {
        
        if (page == null) page = 0;
        if (size == null) size = 10;
        
        Pageable pageable = PageRequest.of(page, size);
        return friendListRepository.findByMemberId(memberId, pageable).map(FriendListResponseDTO::new);
    }

    // 친구 삭제
    @Transactional
    public String removeFriend(Long memberId, Long friendId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_NOT_FOUND.getMessage()));
        Member friend = memberRepository.findById(friendId)
                .orElseThrow(() -> new CustomException(FriendException.FRIEND_NOT_FOUND.getMessage()));
        
        if (!friendListRepository.existsByMemberIdAndFriendId(memberId, friendId)) {
            throw new CustomException(FriendException.FRIEND_NOT_FRIEND.getMessage());
        }
        
        friendListRepository.deleteByMemberAndFriend(member, friend);
        friendListRepository.deleteByMemberAndFriend(friend, member);
        
        return "친구가 성공적으로 삭제되었습니다.";
    }

    // 친구 요청 유효성 검사
    private void validateFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new CustomException(FriendException.SELF_REQUEST_NOT_ALLOWED.getMessage());
        }

        if (friendListRepository.existsByMemberIdAndFriendId(senderId, receiverId)) {
            throw new CustomException(FriendException.FRIEND_ALREADY_EXISTS.getMessage());
        }

        if (existsPendingRequest(senderId, receiverId)) {
            throw new CustomException(FriendException.FRIEND_REQUEST_ALREADY_SENT.getMessage());
        }
    }

    // 친구 관계 생성
    private void createFriendRelation(Member member, Member friend) {
        FriendList friendList = FriendList.builder()
                .member(member)
                .friend(friend)
                .build();
        
        friendListRepository.save(friendList);
        log.debug("친구 관계 생성 - 회원: {}, 친구: {}", member.getId(), friend.getId());
    }

    // 양방향 친구 관계 설정
    private void createMutualFriendship(Member member1, Member member2) {
        createFriendRelation(member1, member2);
        createFriendRelation(member2, member1);
    }

    // 친구 요청 존재 여부 확인
    private boolean existsPendingRequest(Long senderId, Long receiverId) {
        return friendRequestRepository.existsBySenderIdAndReceiverIdAndRequestStatus(
            senderId, 
            receiverId, 
            RequestStatus.PENDING
        );
    }

    // 친구 관계 상태 확인
    public FriendshipStatus getFriendshipStatus(Long memberId, Long targetId) {
        if (friendListRepository.existsByMemberIdAndFriendId(memberId, targetId)) {
            return FriendshipStatus.FRIEND;
        }
        
        if (friendRequestRepository.existsBySenderIdAndReceiverIdAndRequestStatus(
                memberId, targetId, RequestStatus.PENDING)) {
            return FriendshipStatus.PENDING;
        }
        
        if (friendRequestRepository.existsBySenderIdAndReceiverIdAndRequestStatus(
                targetId, memberId, RequestStatus.PENDING)) {
            return FriendshipStatus.PENDING;
        }
        
        return FriendshipStatus.NOT_FRIEND;
    }
}
