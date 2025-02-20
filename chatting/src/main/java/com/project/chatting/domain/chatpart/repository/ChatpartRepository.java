package com.project.chatting.domain.chatpart.repository;

import com.project.chatting.domain.chatpart.entity.Chatpart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatpartRepository extends JpaRepository<Chatpart, Long> {

    @Query("SELECT cp.chatroom.chatroomNo " +
                    "FROM Chatpart cp " +
                    "WHERE cp.user.userNo = :sender OR cp.user.userNo = :receiver " +
                    "GROUP BY cp.chatroom.chatroomNo " +
                    "HAVING COUNT(DISTINCT cp.user) = 2")
    Long findByChatpart(@Param("sender") Long sender, @Param("receiver") Long receiver);

    @Query("SELECT cp " +
                    "FROM Chatpart cp " +
                    "WHERE cp.user.username != :username " +
                    "AND cp.chatroom IN (" +
                    "  SELECT c.chatroom " +
                    "  FROM Chatpart c " +
                    "  WHERE c.user.username = :username" +
                    ") " +
                    "ORDER BY cp.chatroom.updateAt DESC")
    List<Chatpart> getChatlist(String username);
}
