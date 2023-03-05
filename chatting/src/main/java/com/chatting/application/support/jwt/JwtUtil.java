package com.chatting.application.support.jwt;

import java.util.Enumeration;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public final class JwtUtil {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer";

	private JwtUtil() {
	}

	public static Optional<String> extractAuthenticationParam(final HttpServletRequest request) {
		final Enumeration<String> headers = request.getHeaders(AUTHORIZATION_HEADER);
		String authenticationParam = null;
		while (headers.hasMoreElements()) {
			authenticationParam = getAuthenticationParam(headers, authenticationParam);
		}
		return Optional.ofNullable(authenticationParam);
	}

	private static String getAuthenticationParam(Enumeration<String> headers, String authenticationParam) {
		final String value = headers.nextElement();
		if (value.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
			authenticationParam = value.split(" ")[1].trim();
		}
		return authenticationParam;
	}
}
