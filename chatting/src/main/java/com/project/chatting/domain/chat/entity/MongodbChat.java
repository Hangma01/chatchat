package com.project.chatting.domain.chat.entity;

import com.project.chatting.domain.chatroom.entity.ChatRoom;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat")
public class MongodbChat {

    @Id
    private ObjectId chatNo;

    private Long chatroomNo;
    private Long sender;
    private String message;

    @CreatedDate
    private LocalDateTime createAt;


    @Builder
    public MongodbChat(Long chatroomNo, Long sender, String message) {
        this.chatroomNo = chatroomNo;
        this.sender = sender;
        this.message = message;
    }
}
