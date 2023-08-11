package com.chatting.presentation;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatting.application.WebSocketService;
import com.chatting.presentation.dto.WebSocketMessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
public class WebSocketController {

	private final SimpMessageSendingOperations messageSendingOperations;
	private final WebSocketService webSocketService;
	@PostMapping(value = "/sendMessage")
	public void sendMessage(@RequestBody WebSocketMessageDto webSocketMessageDto){
		webSocketService.sendMessageByWebSocket(webSocketMessageDto);

	}
}
