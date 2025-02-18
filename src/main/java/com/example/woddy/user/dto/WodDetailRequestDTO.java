package com.example.woddy.user.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WodDetailRequestDTO {
    @NotEmpty
    private Long workoutId;
    private List<UnitRequestDTO> records; // 기록
}
