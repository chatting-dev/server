package com.chatting.application.support.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.chatting.exception.ErrorCode;
import com.chatting.exception.InvalidTokenException;
import com.chatting.exception.TokenExpirationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProvider {

	private static final String AUTHORITY_KEY = "auth";

	private final SecretKey key;
	private final long expirationMilliseconds;

	public JwtTokenProvider(
		@Value("${security.jwt.token.secret-key}") final String secretKey,
		@Value("${security.jwt.token.expiration}") final long expirationMilliseconds
	) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.expirationMilliseconds = expirationMilliseconds;
	}

	public String createToken(final String payload, final Collection<? extends GrantedAuthority> authorities) {
		final Date now = new Date();
		final Date exp = new Date(now.getTime() + expirationMilliseconds);

		final String authValue = authorities
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		return Jwts.builder()
			.setSubject(payload)
			.claim(AUTHORITY_KEY, authValue)
			.setIssuedAt(now)
			.setExpiration(exp)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Authentication getAuthenticationFromToken(final String token) {
		final Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		if (claims.get(AUTHORITY_KEY) == null) {
			throw new InvalidTokenException("유효하지 않은 토큰입니다.", ErrorCode.JWT_INVALID);
		}

		List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITY_KEY, String.class).split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
	}

	public String expireToken(final String token) {
		Jws<Claims> jws = tokenToJws(token);

		Claims claims = jws.getBody();
		claims.setExpiration(new Date());

		return Jwts.builder()
			.setClaims(claims)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public void validateToken(final String token) {
		final Jws<Claims> claims = tokenToJws(token);
		validateExpiredToken(claims);
	}

	private Jws<Claims> tokenToJws(final String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
		} catch (final IllegalArgumentException | SecurityException | MalformedJwtException e) {
			throw new InvalidTokenException("잘못된 JWT 서명입니다.", ErrorCode.JWT_INVALID);
		} catch (final UnsupportedJwtException e) {
			throw new InvalidTokenException("지원하지 않는 JWT 입니다.", ErrorCode.JWT_INVALID);
		} catch (final SignatureException e) {
			throw new InvalidTokenException("토큰의 SECRET KEY가 변조되었습니다.", ErrorCode.JWT_INVALID);
		} catch (final ExpiredJwtException e) {
			throw new InvalidTokenException("만료된 JWT 토큰입니다.", ErrorCode.JWT_EXPIRED);
		}
	}

	private void validateExpiredToken(final Jws<Claims> claims) {
		if (claims.getBody().getExpiration().before(new Date())) {
			throw new TokenExpirationException("토큰의 유효기간이 만료되었습니다.", ErrorCode.JWT_EXPIRED);
		}
	}
}
