package com.project.chatting.domain.chatpart.controller;


import com.project.chatting.domain.chatpart.service.ChatpartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat-part")
@RequiredArgsConstructor
public class ChatpartController {

    private final ChatpartService chatpartService;

    @GetMapping("/chatlist")
    public ResponseEntity<?> getChatlist() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(chatpartService.getChatlist(username),HttpStatus.OK);
    }

}
