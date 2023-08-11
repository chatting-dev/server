package com.chatting.presentation.dto;

public record WebSocketMessageDto(
	String senderId,
	String messageBody,
	String timestamp,
	String receiverId
) {
}
