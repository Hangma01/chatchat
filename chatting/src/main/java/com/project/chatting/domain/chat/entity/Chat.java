package com.project.chatting.domain.chat.entity;

import com.project.chatting.domain.chatroom.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatNo", updatable = false)
    private Long chatNo;

    @Column(name = "sender")
    private Long sender;

    @Column(name = "message")
    private String message;

    @Column(name = "createAt")
    @CreatedDate
    private LocalDateTime createAt;

    // ChatRoom과의 관계 설정
    @ManyToOne
    @JoinColumn(name = "chatroomNo") // 외래키 컬럼 이름 지정
    private ChatRoom chatroom;

    @Builder
    public Chat(Long sender, String message, ChatRoom chatroom) {
        this.sender = sender;
        this.message = message;
        this.chatroom = chatroom;
    }
}
