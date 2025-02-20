package com.project.chatting.domain.chatpart.dto;

import com.project.chatting.domain.chatpart.entity.Chatpart;
import com.project.chatting.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ResponseChatlist {
    private Long chatroomNo;
    private String lastMessage;
    private String username;
    private String nickname;



    public ResponseChatlist(Long chatroomNo, String lastMessage, String username, String nickname) {
        this.chatroomNo = chatroomNo;
        this.lastMessage = lastMessage;
        this.username = username;
        this.nickname = nickname;
    }
}
