package com.chatting.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatting.domain.useraccount.UserAccountRepository;
import com.chatting.presentation.dto.UserAccountDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

	private final UserAccountRepository userAccountRepository;

	public Optional<UserAccountDto> searchUserAccount(final String loginId) {
		return userAccountRepository.findByLoginId(loginId)
			.map(UserAccountDto::from);
	}
}
