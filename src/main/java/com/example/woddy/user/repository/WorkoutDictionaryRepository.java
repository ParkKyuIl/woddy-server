package com.example.woddy.user.repository;

import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutDictionaryRepository extends JpaRepository<WorkoutDictionaryEntity, Long> {
    List<WorkoutDictionaryEntity> findAll();
}
