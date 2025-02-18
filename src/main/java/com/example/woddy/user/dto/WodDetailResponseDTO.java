package com.example.woddy.user.dto;

import com.example.woddy.user.enums.WeightOption;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WodDetailResponseDTO {

    private Long id; // 와드 상세 정보 아이디 (PK)
    private Long wodId; // 와드 정보 아이디 (FK)
    private Long workoutId; // 운동 아이디 (FK)
    private Integer repetition; // 반복 횟수
    private Integer calorie; // 칼로리
    private WeightOption weightOption; // 무게 옵션 (ENUM)
    private Integer weightDirectInput; // 무게 직접입력 값
    private LocalDateTime createdAt; // 생성된 날짜
    private LocalDateTime updatedAt; // 업데이트된 날짜

}
