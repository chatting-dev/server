package com.chatting.presentation.dto.request;

public record SendMessageRequest(
	String senderId,
	String messageBody,
	String receiverId
) {
}
