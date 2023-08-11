package com.chatting.application;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatting.presentation.dto.WebSocketMessageDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WebSocketService {
	private final SimpMessageSendingOperations messagingTemplate;

	public void sendMessageByWebSocket(WebSocketMessageDto webSocketMessageDto) {
		messagingTemplate.convertAndSend("/topic/" + webSocketMessageDto.receiverId()
			, webSocketMessageDto);
	}
}
