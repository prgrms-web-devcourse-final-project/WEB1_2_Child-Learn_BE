package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.friend.entity.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {
    List<FriendList> findByMemberId(Long memberId);
    void deleteByMemberIdAndFriendId(Long memberId, Long friendId);
} 
