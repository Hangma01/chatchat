package com.project.chatting.domain.chatroom.controller;

import com.project.chatting.domain.chatpart.service.ChatpartService;
import com.project.chatting.domain.chatroom.dto.RequestCreateChatroom;
import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.chatroom.service.ChatRoomService;
import com.project.chatting.domain.user.Service.UserService;
import com.project.chatting.domain.user.dto.CustomUserDetails;
import com.project.chatting.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatpartService chatpartService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createChatroom(@RequestBody RequestCreateChatroom requestCreateChatroom) {

        Long sender = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserNo();

        // 채팅방이 존재하는지 확인
        Long chatroomNo = chatpartService.findByChatpart(sender, requestCreateChatroom);
        Map<String, Object> response = new HashMap<>();

        // 채팅방이 없으면 채팅방 생성후 번호 넘기기
        if(chatroomNo == null) {
            ChatRoom addChatRoom = chatRoomService.save();
            User addSender =  userService.findById(sender);
            User addReciver = userService.findById(requestCreateChatroom.getUserNo()) ;
            chatpartService.save(addChatRoom, addSender, addReciver);

            response.put("message", "new");
            response.put("chatroomNo", addChatRoom.getChatroomNo());

        }else{
            response.put("chatroomNo", chatroomNo);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
