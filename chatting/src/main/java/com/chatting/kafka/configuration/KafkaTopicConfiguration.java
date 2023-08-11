//package com.chatting.kafka.configuration;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaTopicConfiguration {
//
//    @Bean
//    public NewTopic compactTopic() {
//        return TopicBuilder.name("my-first-compact-kafka-topic")
//                .partitions(6)
//                .replicas(3)
//                .compact().build();
//    }
//}
