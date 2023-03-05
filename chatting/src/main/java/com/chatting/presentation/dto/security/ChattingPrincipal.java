package com.chatting.presentation.dto.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chatting.domain.useraccount.UserAccountRole;
import com.chatting.presentation.dto.UserAccountDto;

public record ChattingPrincipal(
	String email,
	String loginId,
	String password,
	Collection<? extends GrantedAuthority> authorities,
	String nickname,
	String profileUrl
) implements UserDetails {

	public static ChattingPrincipal from(final UserAccountDto userAccountDto) {
		Set<UserAccountRole> roles = Set.of(userAccountDto.userAccountRole());

		return new ChattingPrincipal(
			userAccountDto.email(),
			userAccountDto.loginId(),
			userAccountDto.password(),
			roles.stream()
				.map(UserAccountRole::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toUnmodifiableSet()),
			userAccountDto.nickname(),
			userAccountDto.profileUrl()
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
