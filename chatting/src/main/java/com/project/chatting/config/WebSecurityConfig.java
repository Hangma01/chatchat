package com.project.chatting.config;

import com.project.chatting.domain.jwt.Filter.CustomLogoutFilter;
import com.project.chatting.domain.jwt.Filter.JWTFilter;
import com.project.chatting.domain.jwt.Util.JWTUtil;
import com.project.chatting.domain.jwt.Filter.LoginFilter;
import com.project.chatting.domain.redis.Repository.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    // AuthenticationManager가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    private final RedisRepository redisRepository;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

        http
                // cors 설정
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))

                // csrf disable
                .csrf(AbstractHttpConfigurer::disable)

                // form 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)

                // http basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)

                // 인가 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login", "/join", "/duplicationId","/reissue", "/ws").permitAll()
                        .requestMatchers("/userlist", "/chat-room/*", "/chat/*", "/message", "/chatgpt/**").hasRole("USER")
                        .anyRequest().authenticated())

                // 세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // LoginFilter() 전에 JWTFilter() 추가
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)

                // 필터 추가 LoginFilter()는 인자를 받음
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisRepository), UsernamePasswordAuthenticationFilter.class)

                // LogoutFilter() 전에 CustomLogoutFilter() 추가
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisRepository), LogoutFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
