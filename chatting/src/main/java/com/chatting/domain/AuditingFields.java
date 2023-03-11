package com.chatting.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	protected LocalDateTime createdAt;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "modified_at", nullable = false)
	@LastModifiedDate
	protected LocalDateTime modifiedAt;
}
