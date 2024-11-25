package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.friend.entity.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendListRepository extends JpaRepository<FriendList, Long> { // 친구 목록
    List<FriendList> findAllByMemberIdOrFriendId(Long memberId, Long friendId);
    void deleteByMemberIdAndFriendId(Long memberId, Long friendId);
} 