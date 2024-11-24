package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.friend.entity.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendListRepository extends JpaRepository<FriendList, Long> {
    List<FriendList> findBySenderId(Long senderId);
    List<FriendList> findByReceiverId(Long receiverId);
} 