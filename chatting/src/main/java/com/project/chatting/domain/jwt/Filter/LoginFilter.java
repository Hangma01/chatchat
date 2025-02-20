package com.project.chatting.domain.jwt.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatting.domain.jwt.Util.CookieUtil;
import com.project.chatting.domain.jwt.Util.JWTUtil;
import com.project.chatting.domain.redis.Entity.RefreshToken;
import com.project.chatting.domain.redis.Repository.RedisRepository;
import com.project.chatting.domain.user.dto.CustomUserDetails;
import com.project.chatting.domain.user.dto.RequestUserLogin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisRepository redisRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            RequestUserLogin requestUserLogin = objectMapper.readValue(request.getInputStream(), RequestUserLogin.class);

            // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 한다.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestUserLogin.getUsername(), requestUserLogin.getPassword());

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 시 실행하는 메소드 (여기서 JWT 발급함)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){

        String username = authentication.getName();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userNo = customUserDetails.getUserNo();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", userNo, username, role, 3600000L);      // 1시간
        String refresh = jwtUtil.createJwt("refresh", userNo, username, role, 86400000L);   // 24시간

        // Redis에 Refresh 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                                .username(username)
                                                .refreshToken(refresh)
                                                .build();

        redisRepository.save(refreshToken);

        // 응답 설정
        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(CookieUtil.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

    }

    // 로그인 실패 시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


}
