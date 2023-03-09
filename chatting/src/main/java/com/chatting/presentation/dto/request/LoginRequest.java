package com.chatting.presentation.dto.request;

public record LoginRequest(
	String loginId,
	String password
) {
}
