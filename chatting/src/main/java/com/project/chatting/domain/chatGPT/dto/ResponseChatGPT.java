package com.project.chatting.domain.chatGPT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseChatGPT {
    private List<ChatGPTChoice> choices;
}
