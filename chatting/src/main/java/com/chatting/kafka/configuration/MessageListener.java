package com.chatting.kafka.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chatting.presentation.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(Message message) {
        log.info("sending via kafka listener.."+message.toString());
        template.convertAndSend("/sub/"+message.getTopic()+"/"+message.getChatRoom(), message);
    }
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Scheduled(fixedRate = 1000)
    public void getConnectedUserSessions() {
        List<String> connectedSessions = new ArrayList<>();
        for (SimpUser user : simpUserRegistry.getUsers()) {
            for (SimpSession session : user.getSessions()) {
                System.out.println(session.getId());
                session.getSubscriptions().stream().forEach(subsciption-> System.out.println(subsciption.getDestination()));
                if (session.getSubscriptions().stream().anyMatch(subscription -> subscription.getDestination().equals("/chat/hello"))) {
                    connectedSessions.add(session.getId());
                }
            }
        }
        System.out.println("현재 연결된 세션 Id: "+connectedSessions);
    }

}