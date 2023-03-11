package com.chatting.application.support.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.chatting.application.support.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
		Authentication authentication) {
		String accessToken = jwtTokenProvider.createToken(
			authentication.getName(), authentication.getAuthorities());

		response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
}
