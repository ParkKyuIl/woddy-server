package com.example.woddy.user.service;

import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import com.example.woddy.user.repository.WorkoutDictionaryRepository;
import com.example.woddy.user.utils.CsvHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class WorkoutDataLoader implements CommandLineRunner {

    private final WorkoutDictionaryRepository workoutDictionaryRepository;
    private final ResourceLoader resourceLoader;

    public WorkoutDataLoader(WorkoutDictionaryRepository workoutDictionaryRepository, ResourceLoader resourceLoader) {
        this.workoutDictionaryRepository = workoutDictionaryRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) {
        if (workoutDictionaryRepository.count() == 0) {
            // ResourceLoader를 사용하여 리소스 로드
            Resource resource = resourceLoader.getResource("classpath:workouts.csv");
            try (InputStream inputStream = resource.getInputStream()) {
                List<WorkoutDictionaryEntity> workouts = CsvHelper.csvToWorkouts(inputStream);
                workoutDictionaryRepository.saveAll(workouts);
                log.info("운동 데이터베이스가 초기화되었습니다. 저장된 운동 수: {}", workouts.size());
            } catch (IOException e) {
                log.error("CSV 파일을 읽는 중 오류가 발생했습니다", e);
                throw new RuntimeException("CSV 파일 읽기 실패: " + e.getMessage());
            }
        }
    }
}
