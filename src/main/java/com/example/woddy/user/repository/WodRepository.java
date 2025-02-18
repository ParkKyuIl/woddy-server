package com.example.woddy.user.repository;

import com.example.woddy.user.entity.WodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WodRepository extends JpaRepository<WodEntity,String> {
    List<WodEntity> findByUserId(long userId);
    WodEntity findByid(Long id);

}
