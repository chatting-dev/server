package com.chatting.presentation.dto.request;

import com.chatting.domain.useraccount.UserAccount;

public record SignUpRequest(
	String email,
	String loginId,
	String password,
	String nickname
) {

	public UserAccount toEntity(final String encryptPassword) {
		return UserAccount.of(email, loginId, encryptPassword, nickname);
	}
}
