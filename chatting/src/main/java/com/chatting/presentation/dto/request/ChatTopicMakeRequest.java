package com.chatting.presentation.dto.request;

import com.chatting.domain.topic.Topic;

public record ChatTopicMakeRequest(
	String name
) {
	public Topic toEntity(final String name){
		return Topic.of(name);
	}
}
