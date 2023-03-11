package com.chatting.presentation.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chatting.application.support.jwt.JwtTokenProvider;
import com.chatting.application.support.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain filterChain) throws ServletException, IOException {

		JwtUtil.extractAuthenticationParam(request)
			.ifPresent(token -> {
				jwtTokenProvider.validateToken(token);
				SecurityContextHolder
					.getContext()
					.setAuthentication(jwtTokenProvider.getAuthenticationFromToken(token));
			});

		filterChain.doFilter(request, response);
	}
}
