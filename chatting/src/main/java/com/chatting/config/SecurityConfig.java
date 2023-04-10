package com.chatting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.chatting.application.AuthService;
import com.chatting.application.support.handler.LogoutProcessHandler;
import com.chatting.application.support.jwt.JwtTokenProvider;
import com.chatting.exception.ErrorCode;
import com.chatting.exception.UserNotFoundException;
import com.chatting.presentation.dto.security.ChattingPrincipal;
import com.chatting.presentation.filter.JwtFilter;
import com.chatting.presentation.filter.LoginAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final LoginAuthenticationFilter loginAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/v1/users", "/api/v1/auth","/my-chat")
				.permitAll()
				.anyRequest()
				.authenticated()
			);

		http
			.logout(logout -> logout
				.logoutUrl("/api/v1/logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.logoutSuccessUrl("/api/v1"));

		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http
			.addFilterAt(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutProcessHandler(jwtTokenProvider);
	}

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService(final AuthService authService) {
		return username -> authService
			.searchUserAccount(username)
			.map(ChattingPrincipal::from)
			.orElseThrow(
				() -> new UserNotFoundException("사용자 계정을 찾을 수 없습니다. - username : " + username, ErrorCode.UA_LOGIN));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
