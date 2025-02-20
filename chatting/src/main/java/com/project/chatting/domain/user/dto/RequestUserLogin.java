package com.project.chatting.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUserLogin {

    private String username;
    private String password;
}
