package com.project.chatting.domain.user.controller;

import com.project.chatting.domain.user.Service.UserService;
import com.project.chatting.domain.user.dto.RequestUserJoin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody RequestUserJoin requestUserJoin) {
        
        userService.joinProcess(requestUserJoin);

        return ResponseEntity.ok().build();
    }

    // 중복 아이디 체크
    @GetMapping("/duplicationId")
    public ResponseEntity<Void> DuplicationIdCheck(@RequestParam String username) {
        Boolean result = userService.DuplicationIdCheck(username);
        System.out.println(username);
        if(result){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 리스트 가져오기
    @GetMapping("/userlist")
    public ResponseEntity<?> getUserlist() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.getUserlist(username),HttpStatus.OK);
    }
}
