package com.chatting.domain.topic;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
	boolean existsByName(final String name);
	Topic findByName(final String name);
}
