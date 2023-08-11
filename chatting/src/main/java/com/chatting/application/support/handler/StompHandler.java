package com.chatting.application.support.handler;

import java.nio.file.AccessDeniedException;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.chatting.application.support.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel){
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if(accessor.getCommand()== StompCommand.CONNECT){
			jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"));
		}
		return message;
	}

}
