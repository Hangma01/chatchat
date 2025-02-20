package com.project.chatting.domain.chatGPT.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RequestChatGPT {
    private String model;
    private List<ChatGPTMessage> messages;

    public RequestChatGPT(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new ChatGPTMessage("user", prompt));
    }
}
