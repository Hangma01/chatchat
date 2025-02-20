package com.project.chatting.domain.chatpart.service;

import com.project.chatting.domain.chatpart.dto.ResponseChatlist;
import com.project.chatting.domain.chatpart.entity.Chatpart;
import com.project.chatting.domain.chatpart.repository.ChatpartRepository;
import com.project.chatting.domain.chatroom.dto.RequestCreateChatroom;
import com.project.chatting.domain.chatroom.entity.ChatRoom;
import com.project.chatting.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatpartService {

    private final ChatpartRepository chatpartRepository;

    public Long findByChatpart(Long sender, RequestCreateChatroom requestCreateChatroom) {

        Long receiver = requestCreateChatroom.getUserNo();

        Long chatroomNo = chatpartRepository.findByChatpart(sender, receiver);

        return chatroomNo;
    }

    public void save(ChatRoom chatRooom, User sender, User receiver) {

        List<Chatpart> chatparts = new ArrayList<>();

        chatparts.add(new Chatpart(chatRooom, sender));
        chatparts.add(new Chatpart(chatRooom, receiver));

        chatpartRepository.saveAll(chatparts);
    }

    public List<ResponseChatlist> getChatlist(String username) {
        List<Chatpart> chatlist = chatpartRepository.getChatlist(username);

        if(chatlist == null || chatlist.isEmpty()) {
            return null;
        }

        return chatlist.stream()
                .map(chat -> new ResponseChatlist(
                        chat.getChatroom().getChatroomNo(),
                        chat.getChatroom().getLastMessage() == null ? "" : chat.getChatroom().getLastMessage(),
                        chat.getUser().getUsername(),
                        chat.getUser().getNickname()))
                .collect(Collectors.toList());
    }
}
