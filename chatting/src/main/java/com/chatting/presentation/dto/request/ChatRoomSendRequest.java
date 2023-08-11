package com.chatting.presentation.dto.request;

public record ChatRoomSendRequest(
	String topic,
	String chatRoom,
	String loginId
) {
}
