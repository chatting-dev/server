package com.chatting.presentation.dto.request;

import com.chatting.domain.chatroom.ChatRoom;
import com.chatting.domain.topic.Topic;

public record ChatRoomMakeRequest(
	String name,
	String topic
) {
	public ChatRoom toEntity(final String name, final Topic topic){
		return ChatRoom.of(name, topic);
	}
}
