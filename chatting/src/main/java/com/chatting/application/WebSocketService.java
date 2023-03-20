package com.chatting.application;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatting.domain.useraccount.UserAccountRepository;
import com.chatting.presentation.dto.request.SendMessageRequest;
import com.chatting.presentation.dto.response.SendMessageResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WebSocketService {
	private final SimpMessageSendingOperations messagingTemplate;
	private final UserAccountRepository userAccountRepository;

	public SendMessageResponse sendMessageByWebSocket(final SendMessageRequest sendMessageRequest) {
		if (!userAccountRepository.existsByLoginId(sendMessageRequest.receiverId())) {
			return new SendMessageResponse(false);
		}
		messagingTemplate.convertAndSend("/sub/" + sendMessageRequest.receiverId()
			, sendMessageRequest);
		return new SendMessageResponse(true);

	}
}
