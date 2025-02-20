package com.project.chatting.domain.user.repository;

import com.project.chatting.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username != :username ORDER BY u.nickname")
    List<User> getByUserlist(@Param("username") String username);

    Boolean existsByUsername(String username);
}
