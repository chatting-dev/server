package com.chatting.application;

import static com.chatting.fixture.FixtureFactory.*;
import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.chatting.DatabaseInitializerExtension;
import com.chatting.application.support.jwt.JwtTokenProvider;
import com.chatting.presentation.dto.Message;
import com.chatting.presentation.dto.WebSocketMessageDto;
import com.chatting.presentation.dto.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("WebSocketService 클래스 테스트")
@SpringBootTest
@ExtendWith(DatabaseInitializerExtension.class)
public class WebSocketServiceTest {

	static final String WEBSOCKET_TOPIC = "/sub/";
	static final Integer PORT = 8080;
	WebSocketStompClient stompClient;
	BlockingQueue<String> blockingQueue;

	private static final String SECRET_KEY = "7e00f61f4b541f55d5a4007e71fcf801d7a43eaba41dfc11ca7126314738d61d009cae69be3c7a4986d2f289173d1d605d22268a92e578094eff5c9faf3973eb";
	private static final long EXP_MILLISECONDS = 3600000;

	private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXP_MILLISECONDS);

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private UserAccountService sut;

	@BeforeEach
	void beforeEach(){
		SignUpRequest sendUserRequest =
			SignUpRequest_생성("bruni@email.com", "23Yong", "bruniPassword123!", "bruni");
		SignUpRequest receivedUserRequest =
			SignUpRequest_생성("june@email.com", "JJONSOO", "junePassword123!", "june");
		sut.signUp(sendUserRequest);
		sut.signUp(receivedUserRequest);

		stompClient = new WebSocketStompClient(new SockJsClient(
			Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
	}
	@Test
	@DisplayName("한 명의 유저가 다른 사람에게 메세지를 보냈을 때 올바르게 갔는지 확인한다.")
	void givenTwoUser_whenUserSendWebSocketMessageToUser_thenUserReceiveSOcketMessage() throws Exception { // 메시지 수신 시 알람 테스트

		// given
		String senderLoginId = "23Yong";
		List<SimpleGrantedAuthority> senderAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		String senderToken = jwtTokenProvider.createToken(senderLoginId, senderAuthorities);

		String receiverLoginId = "JJONSOO";
		List<SimpleGrantedAuthority> receiverAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
		String token = jwtTokenProvider.createToken(receiverLoginId, receiverAuthorities);

		StompHeaders headers = new StompHeaders(); // 헤더에 토큰 삽입
		headers.add("Authorization", senderToken);
		// StompSession session1 = stompClient.connectAsync(String.format("http://localhost:%d/chat/test", PORT), new WebSocketHttpHeaders(),
		// 		headers, new StompSessionHandlerAdapter() {
		// 		})
		// 	.get(10, SECONDS);
		StompSession session = stompClient.connect(getWsPath(), new WebSocketHttpHeaders(), headers,
				new StompSessionHandlerAdapter() {
				})
			.get(10, SECONDS); // 연결
		session.subscribe(WEBSOCKET_TOPIC + receiverLoginId, new DefaultStompFrameHandler()); // "/sub/{userId}" 구독

		// when
		// WebSocketMessageDto requestDto = WebSocketMessageDto.from(new Message(senderLoginId,"MESSAGE TEST"), receiverLoginId);
		// webSocketService.sendMessageByWebSocket(requestDto); // 메세지 전송

		// then
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = blockingQueue.poll(10, SECONDS); // 소켓 수신 내역 꺼내옴
		Map<String, String> result = mapper.readValue(jsonResult, Map.class); // json 파싱
		assertThat(result.get("message")).isEqualTo("MESSAGE TEST");
	}


	class DefaultStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			blockingQueue.offer(new String((byte[])o));
		}
	}
	private String getWsPath() {
		return String.format("ws://localhost:%d/my-chat", PORT);
	}

}
