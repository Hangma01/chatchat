package com.project.chatting.domain.chatGPT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatGPTMessage {
    private String role;
    private String content;

}
