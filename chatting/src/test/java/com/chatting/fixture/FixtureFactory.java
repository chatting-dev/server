package com.chatting.fixture;

import com.chatting.presentation.dto.request.SignUpRequest;

public class FixtureFactory {

	public static SignUpRequest SignUpRequest_생성(final String email, final String loginId, final String password,
		final String nickname) {
		return new SignUpRequest(email, loginId, password, nickname);
	}
}
