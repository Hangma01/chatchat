package com.project.chatting.domain.chatpart.dto;


import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.user.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class AddChatpar {


    private ChatRoom chatroom;  // 채팅방에 대한 참조
    private User user;  // 멤버에 대한 참조
}
