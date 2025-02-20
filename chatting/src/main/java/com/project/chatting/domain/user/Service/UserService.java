package com.project.chatting.domain.user.Service;

import com.project.chatting.domain.user.dto.RequestUserJoin;
import com.project.chatting.domain.user.dto.ResponseUserlist;
import com.project.chatting.domain.user.entity.User;
import com.project.chatting.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    public void joinProcess(RequestUserJoin requestUserJoin) {

        userRepository.save(requestUserJoin.toEntity(bCryptPasswordEncoder.encode(requestUserJoin.getPassword()),"ROLE_USER"));
    }

    public List<ResponseUserlist> getUserlist(String username) {

        return userRepository.getByUserlist(username)
                .stream()
                .map(ResponseUserlist::new)
                .toList();
    }


    public Boolean DuplicationIdCheck(String username) {

        return userRepository.existsByUsername(username);
    }

    public User findById(Long userNo) {
        return userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("not found:" + userNo));
    }

}
