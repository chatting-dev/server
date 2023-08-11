package com.chatting.presentation.dto.request;

import com.chatting.presentation.dto.Message;

public record WebSocketMessageRequest(
	Message message,
	String receiverId
) {
}
