package com.chatting.domain.useraccount;

import java.util.Objects;

import com.chatting.domain.AuditingFields;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_account")
@Entity
public class UserAccount extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_id", nullable = false, length = 64)
	private String loginId;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "nickname", nullable = false, length = 64)
	private String nickname;

	@Column(name = "profile_url", nullable = false)
	private String profileUrl;

	@Column(name = "user_account_role", nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private UserAccountRole userAccountRole;

	private UserAccount(final String email, final String loginId, final String password, final String nickname) {
		this.email = email;
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.profileUrl = "";    // TODO : default profile url 설정
		this.userAccountRole = UserAccountRole.USER;
	}

	public static UserAccount of(String email, String loginId, String password, String nickname) {
		return new UserAccount(email, loginId, password, nickname);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserAccount that))
			return false;
		return this.getId() != null && this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
