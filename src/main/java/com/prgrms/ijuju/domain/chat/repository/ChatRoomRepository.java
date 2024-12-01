package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByMemberIdAndFriendIdAndIsDeletedFalse(Long memberId, Long friendId);

    List<ChatRoom> findByMemberIdOrFriendId(Long memberId, Long friendId);

    boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);
} 
