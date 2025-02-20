package com.project.chatting.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userNo", updatable = false)
    private Long userNo;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "role", nullable = false)
    private String role;

    @Builder
    public User(Long userNo, String username, String password, String nickname, String role) {
        this.userNo = userNo;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}

