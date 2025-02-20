package com.project.chatting.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddChatMessage {
    private Long sender;
    private String message;
    private Long chatroomNo;
}
