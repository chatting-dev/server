package com.chatting.kafka.Controller;

import com.chatting.presentation.dto.Message;
import com.chatting.kafka.configuration.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/kafka")
public class ChatController {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @PostMapping(value = "/publish")
    public void sendMessage(@RequestBody Message message){
        log.info("Produce message : " + message.toString());
        message.setTimestamp(LocalDateTime.now().toString());
        try{
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
