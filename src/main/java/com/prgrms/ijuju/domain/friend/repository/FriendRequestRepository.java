package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiverUsernameAndRequestStatus(String username, RequestStatus status);

    List<FriendRequest> findBySenderUsernameAndRequestStatus(String username, RequestStatus status);

    Optional<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);
    
    boolean existsBySenderUsernameAndReceiverUsernameAndRequestStatus(
        String senderUsername, 
        String receiverUsername, 
        RequestStatus requestStatus
    );

    boolean existsBySenderAndReceiverAndRequestStatus(Member sender, Member receiver, RequestStatus requestStatus);

    Optional<FriendRequest> findBySenderAndReceiverAndRequestStatus(Member sender, Member receiver, RequestStatus requestStatus);

    boolean existsBySenderIdAndReceiverIdAndRequestStatus(Long senderId, Long receiverId, RequestStatus requestStatus);

    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<FriendRequest> findBySenderIdAndRequestStatus(Long senderId, RequestStatus requestStatus);

    List<FriendRequest> findByReceiverIdAndRequestStatus(Long receiverId, RequestStatus requestStatus);
}
