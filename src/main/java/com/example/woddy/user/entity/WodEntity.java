package com.example.woddy.user.entity;

import com.example.woddy.user.dto.WodRequestDTO;
import com.example.woddy.user.enums.TimeOfDay;
import com.example.woddy.user.enums.WodType;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import lombok.ToString;

@Entity
@Table(name = "wods")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // WOD의 고유 ID

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;  // 운동한 날짜

    @Enumerated(EnumType.STRING)
    private TimeOfDay timeOfDay;  // 운동한 시간대 (아침, 점심, 저녁)

    private Integer durationInMinutes;  // 전체 운동 시간

    private Integer intensityLevel;  // 운동 강도 (1~5 단계)

    @Enumerated(EnumType.STRING)
    private WodType wodType;

    @OneToMany(mappedBy = "wod", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WodDetailEntity> wodDetails;

    private String pictureUrl;  // 운동 사진의 URL

    @ManyToOne
    private UserEntity user;

    public WodEntity(long id) {
        this.id = id;
    }

    public static WodEntity parseDtoToEntity(WodRequestDTO wodRequest) {
        List<WodDetailEntity> wodDetailEntities = wodRequest.getWodDetails().stream().map(WodDetailEntity::parseDtoToEntity).collect(
            Collectors.toList());
        UserEntity user = new UserEntity(wodRequest.getUserId());

        return WodEntity.builder().date(wodRequest.getDate()).timeOfDay(wodRequest.getTimeOfDay())
            .durationInMinutes(wodRequest.getDurationInMinutes()).intensityLevel(wodRequest.getIntensityLevel()).wodType(wodRequest.getWodType())
            .wodDetails(wodDetailEntities).pictureUrl(wodRequest.getPictureUrl()).user(user).build();
    }
}
