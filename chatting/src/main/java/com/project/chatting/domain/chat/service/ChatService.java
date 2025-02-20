package com.project.chatting.domain.chat.service;

import com.project.chatting.domain.chat.dto.AddChatMessage;
import com.project.chatting.domain.chat.dto.ResponseChatMessage;
import com.project.chatting.domain.chat.entity.Chat;
import com.project.chatting.domain.chat.entity.MongodbChat;
import com.project.chatting.domain.chat.repository.ChatRepository;
import com.project.chatting.domain.chat.repository.MongodbChatRepository;
import com.project.chatting.domain.chatroom.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MongodbChatRepository mongodbChatRepository;

    public ResponseChatMessage save(AddChatMessage addChatMessage, ChatRoom chatRoom) {

//        Chat chatSave = chatRepository.save(Chat.builder()
//                .sender(addChatMessage.getSender())
//                .message(addChatMessage.getMessage())
//                .chatroom(chatRoom)
//                .build());

        MongodbChat mongodbChatSave = mongodbChatRepository.save(MongodbChat.builder()
                .chatroomNo(addChatMessage.getChatroomNo())
                .sender(addChatMessage.getSender())
                .message(addChatMessage.getMessage())
                .build());


        return ResponseChatMessage.of(mongodbChatSave);
    }

    public List<ResponseChatMessage> getChatMessage(Long chatroomNo) {


        // 채팅 메시지 리스트 가져오기
        List<MongodbChat> mongodbChats = mongodbChatRepository.getChatMessage(chatroomNo);

        // 채팅 메시지가 없으면 null 반환
        if(mongodbChats == null || mongodbChats.isEmpty()) {
            return null;
        }

        return mongodbChats.stream()
                .map(chat -> new ResponseChatMessage(chat.getSender(), chat.getMessage()))
                .collect(Collectors.toList());
    }
}
