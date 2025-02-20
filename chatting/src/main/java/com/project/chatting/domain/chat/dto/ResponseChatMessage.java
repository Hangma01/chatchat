package com.project.chatting.domain.chat.dto;

import com.project.chatting.domain.chat.entity.Chat;
import com.project.chatting.domain.chat.entity.MongodbChat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResponseChatMessage {
    private Long sender;
    private String message;

    public static ResponseChatMessage of(Chat chat) {
        return ResponseChatMessage.builder()
                .sender(chat.getSender())
                .message(chat.getMessage())
                .build();
    }

    public static ResponseChatMessage of(MongodbChat mongodbChat) {
        return ResponseChatMessage.builder()
                .sender(mongodbChat.getSender())
                .message(mongodbChat.getMessage())
                .build();
    }
}
