package com.project.chatting.domain.chat.controller;

import com.project.chatting.domain.chat.dto.AddChatMessage;
import com.project.chatting.domain.chat.dto.ResponseChatMessage;
import com.project.chatting.domain.chat.entity.MongodbChat;
import com.project.chatting.domain.chat.repository.MongodbChatRepository;
import com.project.chatting.domain.chat.service.ChatService;
import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.chatroom.service.ChatRoomService;
import com.project.chatting.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    // 채팅 리스트 반환
    @GetMapping("/chat")
    public ResponseEntity<?> getChatMessage(@RequestParam Long chatroomNo){

        return new ResponseEntity<>(chatService.getChatMessage(chatroomNo),HttpStatus.OK);
    }

    // 메시지 송신 및 수신, /pub가 생략된 모습. 클라이언트 단에선 /pub/message로 요청
    @MessageMapping("/message")
    public ResponseEntity<Void> receiveMessage(@RequestBody AddChatMessage addChatMessage){

        // 채팅 저장
        ChatRoom chatRoom = chatRoomService.findById(addChatMessage.getChatroomNo());

        ResponseChatMessage responseChatMessage = chatService.save(addChatMessage, chatRoom);

        chatRoomService.update(chatRoom, addChatMessage.getMessage());

        // 메시지를 해당 채팅방 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/chatroom/" + addChatMessage.getChatroomNo(), responseChatMessage);

        return ResponseEntity.ok().build();
    }
}
