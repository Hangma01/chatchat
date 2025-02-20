package com.project.chatting.domain.jwt.controller;

import com.project.chatting.domain.jwt.Util.CookieUtil;
import com.project.chatting.domain.jwt.Util.JWTUtil;
import com.project.chatting.domain.redis.Entity.RefreshToken;
import com.project.chatting.domain.redis.Repository.RedisRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("reissue");
        // 쿠키에서 refresh 토큰 가져오기
        String refresh = null;

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
                System.out.println(refresh);
            }
        }

        if(refresh == null) {
            
            // response status code
            return new ResponseEntity<>("refresh token null",HttpStatus.BAD_REQUEST);
        }
       
        // 만료 여부 확인
        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){

            // response status code
            return new ResponseEntity<>("refresh token expired",HttpStatus.BAD_REQUEST);
        }
      
        // 토큰이 refresh 인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")) {

            // response status code
            return new ResponseEntity<>("invalid refresh token",HttpStatus.BAD_REQUEST);
        }

        // Redis에 저장된 Refresh Token 존재하는지 체크
        Boolean isExist = redisRepository.existsByRefreshToken(refresh);
        if(!isExist){

            // response status code
            return new ResponseEntity<>("refresh token not Exist in redis",HttpStatus.BAD_REQUEST);
        }

        Long userNo = jwtUtil.getUserNo(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // make new JWT
        String newAccess = jwtUtil.createJwt("access", userNo, username, role, 3600000L);    // 1시간
        String newRefresh = jwtUtil.createJwt("refresh", userNo, username, role, 86400000L); // 24시간

        // Redis에 Refresh 토큰 업데이트
        RefreshToken refreshToken = redisRepository.findByUsername(username);
        refreshToken.RefreshTokenUpdate(newRefresh);

        redisRepository.save(refreshToken);

        // response
        response.setHeader("Authorization","Bearer " + newAccess);
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh));

        return ResponseEntity.ok().build();
    }
}
