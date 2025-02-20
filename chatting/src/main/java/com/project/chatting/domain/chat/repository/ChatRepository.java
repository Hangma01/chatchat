package com.project.chatting.domain.chat.repository;

import com.project.chatting.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.chatroom.chatroomNo = :chatroomNo")
    List<Chat> getChatMessage(@Param("chatroomNo") Long chatroomNo);
}
