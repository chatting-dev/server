package com.chatting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChattingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChattingApplication.class, args);
	}

}
