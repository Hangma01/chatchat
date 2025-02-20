package com.project.chatting.domain.chatroom.service;

import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom save() {
        return chatRoomRepository.save(new ChatRoom());
    }

    public ChatRoom findById(Long chatroomNo) {
        return chatRoomRepository.findById(chatroomNo)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    }

    public ChatRoom update(ChatRoom chatRoom, String lastMessage) {

        chatRoom.update(lastMessage);
        return chatRoomRepository.save(chatRoom);
    }
}
