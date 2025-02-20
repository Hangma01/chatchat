package com.project.chatting.domain.user.dto;

import com.project.chatting.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ResponseUserlist {
    private Long userNo;
    private String username;
    private String nickname;

    public ResponseUserlist(User user) {
        this.userNo = user.getUserNo();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
