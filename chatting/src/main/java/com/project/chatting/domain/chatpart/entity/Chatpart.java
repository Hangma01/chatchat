package com.project.chatting.domain.chatpart.entity;

import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chatpart")
@NoArgsConstructor
public class Chatpart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatpartNo")
    private Long chatpartNo;

    @ManyToOne
    @JoinColumn(name = "chatroomNo")
    private ChatRoom chatroom;  // 채팅방에 대한 참조

    @ManyToOne
    @JoinColumn(name = "userNo")
    private User user;  // 멤버에 대한 참조

    public Chatpart(ChatRoom chatroom, User user) {
        this.chatroom = chatroom;
        this.user = user;
    }
}
