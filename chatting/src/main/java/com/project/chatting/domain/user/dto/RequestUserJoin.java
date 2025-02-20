package com.project.chatting.domain.user.dto;

import com.project.chatting.domain.user.entity.User;
import lombok.Getter;

@Getter
public class RequestUserJoin {

    private String username;
    private String password;
    private String nickname;

    public User toEntity(String password, String role){
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
