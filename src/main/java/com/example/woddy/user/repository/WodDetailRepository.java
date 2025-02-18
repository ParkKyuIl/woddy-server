package com.example.woddy.user.repository;

import com.example.woddy.user.entity.WodDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WodDetailRepository extends JpaRepository<WodDetailEntity, Long> {
    List<WodDetailEntity> findByWodId(Long wodId);
}
