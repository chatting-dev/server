package com.chatting.application.support.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

	// private List<WebSocketSession> sessions = new ArrayList<>();
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session.getId(),session);
		System.out.println("WebSocket connected: " + session.getId());
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		System.out.println("WebSocket message received: " + message.getPayload());
		for (WebSocketSession s : sessions.values()) {
			s.sendMessage(new TextMessage("Server: " + message.getPayload()));
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("WebSocket transport error: " + exception.getMessage());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket disconnected: " + session.getId());
		sessions.remove(session.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	@Scheduled(fixedRate = 1000)
	public void expire(){
		System.out.println("보낸다");

		sessions.values().forEach(webSocketSession ->{
			try{
			webSocketSession.sendMessage(new PingMessage());
			}catch (IOException e){
			}
		});
	}
}