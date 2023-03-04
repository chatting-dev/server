package com.chatting.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatting.domain.useraccount.UserAccount;
import com.chatting.domain.useraccount.UserAccountRepository;
import com.chatting.exception.BusinessException;
import com.chatting.presentation.dto.request.SignUpRequest;
import com.chatting.presentation.dto.response.SignUpResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserAccountService {

	private final PasswordEncoder passwordEncoder;
	private final UserAccountRepository userAccountRepository;

	@Transactional
	public SignUpResponse signUp(final SignUpRequest request) {
		validate(request);
		UserAccount userAccount = request.toEntity(passwordEncoder.encode(request.password()));
		userAccountRepository.save(userAccount);

		return new SignUpResponse(true);
	}

	private void validate(final SignUpRequest request) {
		if (userAccountRepository.existsByEmail(request.email())) {
			throw new BusinessException("동일한 이메일이 존재합니다.");
		}
		if (userAccountRepository.existsByLoginId(request.loginId())) {
			throw new BusinessException("동일한 로그인 아이디가 존재합니다.");
		}
		if (userAccountRepository.existsByNickname(request.nickname())) {
			throw new BusinessException("동일한 닉네임이 존재합니다.");
		}
	}
}
