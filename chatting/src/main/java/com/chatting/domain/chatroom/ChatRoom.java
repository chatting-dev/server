package com.chatting.domain.chatroom;

import java.util.Objects;

import com.chatting.domain.AuditingFields;
import com.chatting.domain.topic.Topic;

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
@Table(name = "chat_room")
@Entity
public class ChatRoom extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Integer maxCount;

	@JoinColumn(name = "topic_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Topic topic;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChatRoom that))
			return false;
		return this.getId() != null && this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
