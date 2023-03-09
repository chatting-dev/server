package com.chatting.presentation.dto;

import java.time.LocalDateTime;

import com.chatting.domain.useraccount.UserAccount;
import com.chatting.domain.useraccount.UserAccountRole;

public record UserAccountDto(
	Long id,
	String email,
	String loginId,
	String password,
	String nickname,
	String profileUrl,
	UserAccountRole userAccountRole,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static UserAccountDto from(UserAccount entity) {
		return new UserAccountDto(
			entity.getId(),
			entity.getEmail(),
			entity.getLoginId(),
			entity.getPassword(),
			entity.getNickname(),
			entity.getProfileUrl(),
			entity.getUserAccountRole(),
			entity.getCreatedAt(),
			entity.getModifiedAt()
		);
	}
}
