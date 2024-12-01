package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {

    Page<FriendList> findByMemberId(Long memberId, Pageable pageable);

    boolean existsByMemberAndFriend(Member member, Member friend);

    void deleteByMemberAndFriend(Member member, Member friend);

    List<FriendList> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);
} 
