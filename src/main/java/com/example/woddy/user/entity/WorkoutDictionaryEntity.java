package com.example.woddy.user.entity;

import com.example.woddy.user.enums.WodCategoryType;
import com.example.woddy.user.enums.MeasurementType;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Table(name = "workoutdictionary")
@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutDictionaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Workout name in English cannot be null")
    private String workoutNameEnglish;

    @NotNull(message = "Workout name in Korean cannot be null")
    private String workoutNameKorean;

    private String abbreviation;

    @NotNull(message = "First measurement unit cannot be null")
    private MeasurementType firstMeasurementUnit;

    private MeasurementType secondMeasurementUnit;

    @NotNull(message = "First workout category cannot be null")
    private WodCategoryType firstWorkoutCategory;

    private WodCategoryType secondWorkoutCategory;

    public WorkoutDictionaryEntity(long id) {
        this.id = id;
    }
}
