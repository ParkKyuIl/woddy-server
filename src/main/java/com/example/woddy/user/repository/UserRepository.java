package com.example.woddy.user.repository;

import com.example.woddy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    @Query("SELECT u FROM UserEntity u WHERE u.oauthId = :oauthId")
    UserEntity findByOauthId(String oauthId);

}
