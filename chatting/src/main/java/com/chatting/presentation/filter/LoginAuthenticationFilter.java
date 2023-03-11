package com.chatting.presentation.filter;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.chatting.application.support.RequestWrapper;
import com.chatting.application.support.handler.LoginAuthenticationFailureHandler;
import com.chatting.application.support.handler.LoginAuthenticationSuccessHandler;
import com.chatting.presentation.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;

	public LoginAuthenticationFilter(final ObjectMapper objectMapper,
		@Lazy final AuthenticationManager authenticationManager,
		final LoginAuthenticationSuccessHandler successHandler,
		final LoginAuthenticationFailureHandler failureHandler) {
		super("/api/v1/login", authenticationManager);
		this.objectMapper = objectMapper;
		setAuthenticationSuccessHandler(successHandler);
		setAuthenticationFailureHandler(failureHandler);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request,
		final HttpServletResponse response) throws AuthenticationException, IOException {
		final RequestWrapper requestWrapper = new RequestWrapper(request);
		final LoginRequest loginRequest = objectMapper.readValue(requestWrapper.getReader(), LoginRequest.class);
		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequest.loginId(), loginRequest.password());

		return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
	}
}
