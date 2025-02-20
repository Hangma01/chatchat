package com.project.chatting.domain.stomp.util;

import com.project.chatting.domain.jwt.Util.JWTUtil;
import com.project.chatting.domain.user.dto.CustomUserDetails;
import com.project.chatting.domain.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();

        if(command.equals(StompCommand.CONNECT) || command.equals(StompCommand.SUBSCRIBE)) {

            String accessToken = accessor.getFirstNativeHeader("Authorization");

            if(accessToken == null){

                throw new MalformedJwtException("accessToken not found");
            }

            accessToken = accessToken.replace("Bearer ", "");
            try {
                // 토큰이 만료되었는지 확인
                jwtUtil.isExpired(accessToken);
            } catch (ExpiredJwtException e) {

                throw new MalformedJwtException("expired");
            }

            String category = jwtUtil.getCategory(accessToken);
            if (!category.equals("access")) {
                throw new MalformedJwtException("Invalid access token");
            }
        }

        return message;
    }
}
