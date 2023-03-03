package com.chatting.domain.useraccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	boolean existsByEmail(final String email);

	boolean existsByLoginId(final String loginId);

	boolean existsByNickname(final String nickname);
}
