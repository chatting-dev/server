package com.chatting.presentation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
public class Message implements Serializable {
    private String author;
    private String content;
    private String timestamp;
    private String topic;
    private String chatRoom;
    public Message(String author,String topic,String chatRoom){
        this.author = author;
        this.topic = topic;
        this.chatRoom = chatRoom;

    }
    public Message(String author, String content){
        this.author = author;
        this.content = content;
    }
    public Message(String content){
        this.content = content;
    }
    @Override
    public String toString(){
        return "Message{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
