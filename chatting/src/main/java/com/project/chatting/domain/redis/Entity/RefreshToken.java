package com.project.chatting.domain.redis.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value="token", timeToLive = 86400)   // 24시간
public class RefreshToken {
    @Id
    private String username;

    @Indexed
    private String refreshToken;

    @Builder
    public RefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }

    public void RefreshTokenUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
