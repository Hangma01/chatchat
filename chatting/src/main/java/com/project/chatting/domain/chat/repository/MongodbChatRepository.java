package com.project.chatting.domain.chat.repository;

import com.project.chatting.domain.chat.entity.MongodbChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MongodbChatRepository extends MongoRepository<MongodbChat, String> {

    @Query("{'chatroomNo': ?0} ORDER BY createAt DESC")
    List<MongodbChat> getChatMessage(@Param("chatroomNo") Long chatroomNo);
}
