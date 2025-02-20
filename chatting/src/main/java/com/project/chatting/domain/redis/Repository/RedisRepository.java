package com.project.chatting.domain.redis.Repository;

import com.project.chatting.domain.redis.Entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByUsername(String username);
    void deleteByUsername(String username);

    RefreshToken findByRefreshToken(String refresh);
    Boolean existsByRefreshToken(String refresh);
}
