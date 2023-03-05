package com.chatting.domain.useraccount;

import lombok.Getter;

@Getter
public enum UserAccountRole {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String name;

	UserAccountRole(String name) {
		this.name = name;
	}
}
