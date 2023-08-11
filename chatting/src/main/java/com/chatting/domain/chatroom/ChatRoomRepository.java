package com.chatting.domain.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	boolean existsByName(final String name);
	ChatRoom findByName(final String name);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE ChatRoom chatroom SET chatroom.count=:count")
	int updateChatRoomCount(@Param(value = "count") Integer count);
}
