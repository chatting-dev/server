package com.chatting.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatting.application.WebSocketService;
import com.chatting.presentation.dto.request.SendMessageRequest;
import com.chatting.presentation.dto.response.SendMessageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class WebSocketMessageController {
	private final WebSocketService webSocketService;

	@PostMapping("/sendMessage")
	public ResponseEntity<SendMessageResponse> sendMessage(@RequestBody final SendMessageRequest sendMessageRequest) {
		SendMessageResponse response = webSocketService.sendMessageByWebSocket(sendMessageRequest);

		return ResponseEntity
			.ok()
			.body(response);
	}
}

