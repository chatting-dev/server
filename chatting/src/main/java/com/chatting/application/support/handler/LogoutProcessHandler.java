package com.chatting.application.support.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.chatting.application.support.jwt.JwtTokenProvider;
import com.chatting.application.support.jwt.JwtUtil;
import com.chatting.exception.ErrorCode;
import com.chatting.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutProcessHandler implements LogoutSuccessHandler {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtTokenProvider jwtTokenProvider;

	public LogoutProcessHandler(final JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
		final Authentication authentication) {
		String token = JwtUtil.extractAuthenticationParam(request)
			.orElseThrow(() -> new UserNotFoundException("", ErrorCode.JWT_INVALID));

		String expiredToken = jwtTokenProvider.expireToken(token);
		response.setHeader(AUTHORIZATION_HEADER, expiredToken);
	}
}