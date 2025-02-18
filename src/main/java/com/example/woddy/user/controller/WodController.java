package com.example.woddy.user.controller;

import com.example.woddy.user.dto.WodRequestDTO;
import com.example.woddy.user.dto.WodResponseDTO;
import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import com.example.woddy.user.service.WodService;
import com.example.woddy.user.service.WodTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/wod")
@RequiredArgsConstructor
public class WodController {

    private final WodService wodService;
    private final WodTagService wodTagService;


    @Operation(
            summary = "운동 편식 태그 반환",
            description = "유저의 OauthId를 입력받고 문자열의 태그를 반환합니다"
    )
    @GetMapping("tag/{user_id}")
    public String getUserTag(@PathVariable("user_id") long userId) {
        return wodTagService.getUserTag(userId).toString();
    }

    @Operation(
            summary = "단일 와드 생성",
            description = "wodRequestDTO를 입력받아 와드를 DB에 저장하고 와드를 반환합니다."
    )
    @PostMapping
    public ResponseEntity<WodResponseDTO> createWod(@RequestBody WodRequestDTO wodRequestDTO){
        WodResponseDTO createdWod = wodService.createWod(wodRequestDTO);
        return new ResponseEntity<>(createdWod, HttpStatus.CREATED);
    }

    @Operation(
            summary = "특정 유저 와드 모두 반환",
            description = "OauthId를 입력받아 특정 유저의 와드 다수를 모두 반환합니다."
    )
    @GetMapping("user/{user_id}")
    public ResponseEntity<List<WodResponseDTO>> getWodsByUserId(@PathVariable("user_id") long userId) {
        List<WodResponseDTO> userWods = wodService.getWodsByUserId(userId);
        return new ResponseEntity<>(userWods, HttpStatus.OK);
    }

    @Operation(
            summary = "wodId로 와드를 반환",
            description = "wodId를 입력받아 단일 와드를 반환합니다."
    )
    @GetMapping("/{wod_id}")
    public ResponseEntity<WodResponseDTO> getWodById(@PathVariable("wod_id") Long wodId){
        WodResponseDTO wod = wodService.getWodById(wodId);
        return new ResponseEntity<>(wod, HttpStatus.OK);
    }

    @Operation(
            summary = "모든 운동 종목 반환",
            description = "와드가 아닌 모든 운동(e.g. 더블언더, 머슬업)을 반홥합니다. "
    )
    @GetMapping
    public ResponseEntity<List<WorkoutDictionaryEntity>> getAllWorkouts() {
        List<WorkoutDictionaryEntity> workouts = wodService.getAllWorkouts();
        return new ResponseEntity<>(workouts, HttpStatus.OK);
    }


    @Operation(
            summary = "특정 와드 수정",
            description = "특정 wodId를 입력받아 해당 와드를 수정합니다. "
    )
    @PutMapping("/{wod_id}")
    public ResponseEntity<WodResponseDTO> updateWod(@PathVariable("wod_id") Long wodId, @RequestBody WodRequestDTO wodRequestDTO) {
        WodResponseDTO updatedWod = wodService.updateWod(wodId, wodRequestDTO);
        return new ResponseEntity<>(updatedWod, HttpStatus.OK);
    }

    @Operation(
            summary = "특정 와드 삭제",
            description = "wodId를 입력받아 해당 와드를 삭제하고, 해당 와드와 종속된 모든 운동기록을 삭제합니다."
    )
    @DeleteMapping("/{wod_id}")
    public ResponseEntity<Void> deleteWod(@PathVariable("wod_id") Long wodId) {
        wodService.deleteWod(wodId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}




