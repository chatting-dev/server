package com.chatting.application;

import static com.chatting.fixture.FixtureFactory.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.chatting.DatabaseInitializerExtension;
import com.chatting.domain.useraccount.UserAccount;
import com.chatting.domain.useraccount.UserAccountRepository;
import com.chatting.exception.BusinessException;
import com.chatting.presentation.dto.request.SignUpRequest;

@DisplayName("UserAccountService 클래스 테스트")
@SpringBootTest
@ExtendWith(DatabaseInitializerExtension.class)
class UserAccountServiceTest {

	@Autowired
	private UserAccountService sut;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@DisplayName("회원가입을 진행할 때 ")
	@Nested
	class SignUpTest {

		@DisplayName("올바른 이메일, 로그인 아이디, 닉네임으로 가입하면 성공한다.")
		@Test
		void givenSignUpRequest_whenSignUp_thenReturnsNothing() {
			// given
			SignUpRequest request =
				SignUpRequest_생성("bruni@email.com", "23Yong", "thisIsPassword123!", "bruni");

			// when & then
			assertThatCode(() -> sut.signUp(request))
				.doesNotThrowAnyException();
		}

		@DisplayName("중복된 이메일로 가입하면 예외가 발생한다.")
		@Test
		void givenDuplicatedEmail_whenSignUp_thenThrowsException() {
			// given
			SignUpRequest request =
				SignUpRequest_생성("bruni@email.com", "23Yong", "thisIsPassword123!", "bruni");
			SignUpRequest duplicatedEmailRequest =
				SignUpRequest_생성("bruni@email.com", "23Yong2", "thisIsPassword123!", "bruni2");
			UserAccount userAccount = request.toEntity(passwordEncoder.encode(request.password()));

			userAccountRepository.save(userAccount);

			// when & then
			assertThatThrownBy(() -> sut.signUp(duplicatedEmailRequest))
				.isInstanceOf(BusinessException.class);
		}

		@DisplayName("중복된 로그인 아이디로 가입하면 예외가 발생한다.")
		@Test
		void givenDuplicatedLoginId_whenSignUp_thenThrowsException() {
			// given
			SignUpRequest request =
				SignUpRequest_생성("bruni@email.com", "23Yong", "thisIsPassword123!", "bruni");
			SignUpRequest duplicatedLoginIdRequest =
				SignUpRequest_생성("bruni2@email.com", "23Yong", "thisIsPassword123!", "bruni2");
			UserAccount userAccount = request.toEntity(passwordEncoder.encode(request.password()));

			userAccountRepository.save(userAccount);

			// when & then
			assertThatThrownBy(() -> sut.signUp(duplicatedLoginIdRequest))
				.isInstanceOf(BusinessException.class);
		}

		@DisplayName("중복된 닉네임으로 가입하면 예외가 발생한다.")
		@Test
		void givenDuplicatedNickname_whenSignUp_thenThrowsException() {
			// given
			SignUpRequest request =
				SignUpRequest_생성("bruni@email.com", "23Yong", "thisIsPassword123!", "bruni");
			SignUpRequest duplicatedNicknameRequest =
				SignUpRequest_생성("bruni2@email.com", "23Yong2", "thisIsPassword123!", "bruni");
			UserAccount userAccount = request.toEntity(passwordEncoder.encode(request.password()));

			userAccountRepository.save(userAccount);

			// when & then
			assertThatThrownBy(() -> sut.signUp(duplicatedNicknameRequest))
				.isInstanceOf(BusinessException.class);
		}
	}
}