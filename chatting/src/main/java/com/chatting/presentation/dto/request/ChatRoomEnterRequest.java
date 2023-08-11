package com.chatting.presentation.dto.request;

import com.chatting.domain.chatjoin.ChatJoin;
import com.chatting.domain.chatroom.ChatRoom;
import com.chatting.domain.useraccount.UserAccount;

public record ChatRoomEnterRequest(
	String name,
	String loginId
) {
	public ChatJoin toEntity(final ChatRoom chatRoom, final UserAccount userAccount){
		return ChatJoin.of(chatRoom, userAccount);
	}
}
