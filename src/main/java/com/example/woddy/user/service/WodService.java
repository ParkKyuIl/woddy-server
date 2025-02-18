package com.example.woddy.user.service;

import com.example.woddy.user.dto.WodRequestDTO;
import com.example.woddy.user.dto.WodResponseDTO;
import com.example.woddy.user.entity.UnitEntity;
import com.example.woddy.user.entity.WodDetailEntity;
import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import com.example.woddy.user.enums.WodIntensity;
import com.example.woddy.user.repository.WorkoutDictionaryRepository;
import com.example.woddy.user.repository.WodDetailRepository;
import org.springframework.stereotype.Service;
import com.example.woddy.user.entity.WodEntity;
import com.example.woddy.user.repository.WodRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WodService {
    private final WodRepository wodRepository;

    private final WodDetailRepository wodDetailRepository;

    public WodService(WodRepository wodRepository, WodDetailRepository wodDetailRepository, WorkoutDictionaryRepository workoutDictionaryRepository) {
        this.wodRepository = wodRepository;
        this.wodDetailRepository = wodDetailRepository;
        this.workoutDictionaryRepository = workoutDictionaryRepository;
    }

    // WOD 생성
    public WodResponseDTO createWod(WodRequestDTO wodRequestDTO) {
        // WodRequestDTO를 WodEntity로 변환
        WodEntity wodEntity = WodEntity.parseDtoToEntity(wodRequestDTO);

        for (WodDetailEntity wodDetail : wodEntity.getWodDetails()) {
            for (UnitEntity record : wodDetail.getRecords()) {
                record.setWodDetail(wodDetail);
            }
            wodDetail.setWod(wodEntity);
        }

        // 데이터베이스에 저장
        WodEntity savedWod = wodRepository.save(wodEntity);

        // 저장된 엔티티를 WodResponseDTO로 변환하여 반환
        WodIntensity wodIntensity = WodIntensity.fromLevel(savedWod.getIntensityLevel());

        // 조인 된 데이터 반환
        WodEntity fetchedWod = wodRepository.findByid(savedWod.getId());

        WodResponseDTO wodResponseDTO = new WodResponseDTO(
            fetchedWod.getId(),
            fetchedWod.getDate(),
            fetchedWod.getTimeOfDay(),
            fetchedWod.getDurationInMinutes(),
            fetchedWod.getIntensityLevel(),
            wodIntensity.getMessage(),
            fetchedWod.getWodType(),
            fetchedWod.getPictureUrl(),
            fetchedWod.getWodDetails()
        );

        return wodResponseDTO;
    }

    public List<WodResponseDTO> getWodsByUserId(long userId) {
        // WodRepository에서 userId로 조회, 이때 List<WodEntity>가 반환되어야 함

        List<WodEntity> wodEntities = wodRepository.findByUserId(userId);

        // List<WodEntity>를 stream()으로 순회하며 WodResponseDTO로 변환
        return wodEntities.stream()
                .map(wodEntity -> new WodResponseDTO(
                        wodEntity.getId(),
                        wodEntity.getDate(),
                        wodEntity.getTimeOfDay(),
                        wodEntity.getDurationInMinutes(),
                        wodEntity.getIntensityLevel(),
                        WodIntensity.fromLevel(wodEntity.getIntensityLevel()).getMessage(),
                        wodEntity.getWodType(),
                        wodEntity.getPictureUrl(),
                        wodEntity.getWodDetails()
                ) )
                .collect(Collectors.toList());  // DTO로 변환한 결과 반환
    }

    // TODO : WOD id로 조회
    public WodResponseDTO getWodById(Long wodId) {

        WodEntity wod = wodRepository.findByid(wodId);

        return new WodResponseDTO(wod.getId(),
                wod.getDate(),
                wod.getTimeOfDay(),
                wod.getDurationInMinutes(),
                wod.getIntensityLevel(),
                WodIntensity.fromLevel(wod.getIntensityLevel()).getMessage(),
                wod.getWodType(),
                wod.getPictureUrl(),
                wod.getWodDetails()
            );
    }

    // WOD 업데이트
    public WodResponseDTO updateWod(Long id, WodRequestDTO wodRequestDTO) {
        // id로 기존 WOD 조회
        WodEntity existingWod = wodRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("WOD not found with id: " + id));

        // WodRequestDTO의 값으로 WodEntity 업데이트
        existingWod.setDate(wodRequestDTO.getDate());
        existingWod.setTimeOfDay(wodRequestDTO.getTimeOfDay());
        existingWod.setDurationInMinutes(wodRequestDTO.getDurationInMinutes());
        existingWod.setIntensityLevel(wodRequestDTO.getIntensityLevel());
        existingWod.setWodType(wodRequestDTO.getWodType());
        existingWod.setPictureUrl(wodRequestDTO.getPictureUrl());

        // 업데이트된 엔티티를 저장
        WodEntity updatedWod = wodRepository.save(existingWod);

        // WodResponseDTO로 변환하여 반환
        return new WodResponseDTO(
                updatedWod.getId(),
                updatedWod.getDate(),
                updatedWod.getTimeOfDay(),
                updatedWod.getDurationInMinutes(),
                updatedWod.getIntensityLevel(),
                WodIntensity.fromLevel(updatedWod.getIntensityLevel()).getMessage(),
                updatedWod.getWodType(),
                updatedWod.getPictureUrl(),
                updatedWod.getWodDetails()
        );
    }

    private final WorkoutDictionaryRepository workoutDictionaryRepository;

    // 모든 운동을 가져오는 메서드
    public List<WorkoutDictionaryEntity> getAllWorkouts() {
        return workoutDictionaryRepository.findAll();
    }


    public void deleteWod(Long id) {
        WodEntity existingWod = wodRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("WOD not found with id: " + id));

        wodDetailRepository.deleteAll(wodDetailRepository.findByWodId(existingWod.getId()));
        wodRepository.delete(existingWod);
    }
}