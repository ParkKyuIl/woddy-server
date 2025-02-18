package com.example.woddy.user.dto;

import com.example.woddy.user.entity.WodDetailEntity;
import com.example.woddy.user.enums.TimeOfDay;
import com.example.woddy.user.enums.WodType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class WodResponseDTO {
    private Long id;
    private Date date;
    private TimeOfDay timeOfDay;
    private Integer durationInMinutes;
    private Integer intensityLevel;
    private String intensityMessage;  // 운동 강도에 따른 메시지
    private WodType wodType;
    private String pictureUrl;  // 운동 사진의 URL
    private List<WodDetailEntity> wodDetails;
}