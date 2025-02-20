package com.project.chatting.domain.chatGPT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatGPTChoice {
    private int index;
    private ChatGPTMessage message;
}
