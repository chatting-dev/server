package com.chatting.domain.chatjoin;

import java.util.Objects;

import com.chatting.domain.AuditingFields;
import com.chatting.domain.chatroom.ChatRoom;
import com.chatting.domain.useraccount.UserAccount;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_join")
@Entity
public class ChatJoin extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "chat_room_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ChatRoom chatRoom;

	@JoinColumn(name = "user_account_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserAccount userAccount;

	private ChatJoin(final ChatRoom chatRoom, final UserAccount userAccount){
		this.chatRoom = chatRoom;
		this.userAccount = userAccount;
	}
	public static ChatJoin of(final ChatRoom chatRoom, final UserAccount userAccount){
		return new ChatJoin(chatRoom, userAccount);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChatJoin that))
			return false;
		return this.getId() != null && this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
