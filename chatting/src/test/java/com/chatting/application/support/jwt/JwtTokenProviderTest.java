package com.chatting.application.support.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.chatting.exception.InvalidTokenException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class JwtTokenProviderTest {

	private static final String SECRET_KEY = "thisIsSecretKeyForTest-thisIsSecretKeyForTest-thisIsSecretKeyForTest-thisIsSecretKeyForTest";
	private static final long EXP_MILLISECONDS = 100000L;

	private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXP_MILLISECONDS);

	@DisplayName("올바른 시크릿 키와 만료시간이 주어지면 토큰 생성에 성공한다.")
	@Test
	void givenPayloadAndAuthorities_whenCreateToken_thenReturnsToken() {
		// given
		String payload = "testLoginId";
		Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

		// when
		String token = jwtTokenProvider.createToken(payload, authorities);

		// then
		assertThat(token).isNotNull();
	}

	@DisplayName("잘못된 토큰이 주어지면 검증에 실패한다.")
	@Test
	void givenInvalidToken_whenValidateToken_thenThrowsException() {
		// given
		String invalidToken = "thisIsInvalidToken";

		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(invalidToken))
			.isInstanceOf(InvalidTokenException.class);
	}

	@DisplayName("만료된 토큰이 주어지면 검증에 실패한다.")
	@Test
	void givenExpiredToken_whenValidateToken_thenThrowsException() {
		// given
		String expiredToken = Jwts.builder()
			.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
			.setSubject("testLoginId")
			.setExpiration(new Date(new Date().getTime() - 1))
			.compact();

		// when & then
		assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
			.isInstanceOf(InvalidTokenException.class);
	}

	@DisplayName("주어진 토큰의 페이로드와 권한이 일치하는지 검증한다.")
	@Test
	void givenToken_whenGetAuthenticationFromToken_thenReturnsToken() {
		// given
		String payload = "testLoginId";
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		String token = jwtTokenProvider.createToken(payload, authorities);

		// when
		Authentication authentication = jwtTokenProvider.getAuthenticationFromToken(token);

		// then
		assertAll(
			() -> assertThat(authentication.getName()).isEqualTo(payload),
			() -> assertThat(authentication.getAuthorities()).isEqualTo(authorities)
		);
	}
}
