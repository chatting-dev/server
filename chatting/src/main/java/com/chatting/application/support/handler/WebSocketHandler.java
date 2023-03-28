package com.chatting.application.support.handler;

import static org.springframework.web.socket.CloseStatus.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private final Map<String, Integer> receivedPongSessions = new ConcurrentHashMap<>();
	private static final Integer pongCount = 4;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session.getId(), session);
		receivedPongSessions.put(session.getId(), pongCount);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session.getId());
		receivedPongSessions.remove(session.getId());
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage pongMessage) {
		receivedPongSessions.put(session.getId(), pongCount);
	}

	@Scheduled(fixedRate = 1000)
	public void expire() {
		sessions.forEach((webSocketId, webSocketSession) -> {
			try {
				receivedPongSessions.put(webSocketId, receivedPongSessions.get(webSocketId) - 1);
				webSocketSession.sendMessage(new PingMessage());
			} catch (IOException e) {
			}

			if (receivedPongSessions.get(webSocketId) == 0) {
				try {
					webSocketSession.close();
					afterConnectionClosed(webSocketSession, SESSION_NOT_RELIABLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
