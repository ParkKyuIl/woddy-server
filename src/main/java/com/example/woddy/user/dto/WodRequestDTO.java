package com.example.woddy.user.dto;

import com.example.woddy.user.entity.WodDetailEntity;
import com.example.woddy.user.enums.TimeOfDay;
import com.example.woddy.user.enums.WodType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WodRequestDTO {
    @NotNull
    private long userId; // 유저의 아이디

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date cannot be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;  // 운동한 날짜

    @NotNull(message = "Time of day cannot be null")
    private TimeOfDay timeOfDay;  // 운동한 시간대 (아침, 점심, 저녁)

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be between 1 and 120 minutes")
    @Max(value = 120, message = "Duration must be between 1 and 120 minutes")
    private Integer durationInMinutes;  // 전체 운동 시간

    @NotNull(message = "Intensity level cannot be null")
    @Min(value = 1, message = "Intensity level must be between 1 and 5")
    @Max(value = 5, message = "Intensity level must be between 1 and 5")
    private Integer intensityLevel;  // 운동 강도 (1~5 단계)

    private WodType wodType;

    private String pictureUrl;  // 운동 사진의 URL

    private List<WodDetailRequestDTO> wodDetails;
}