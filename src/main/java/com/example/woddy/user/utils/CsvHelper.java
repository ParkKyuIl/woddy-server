package com.example.woddy.user.utils;

import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import com.example.woddy.user.enums.WodCategoryType;
import com.example.woddy.user.enums.MeasurementType;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    public static List<WorkoutDictionaryEntity> csvToWorkouts(InputStream inputStream) {
        List<WorkoutDictionaryEntity> workouts = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] values;
            boolean isHeader = true;
            while ((values = csvReader.readNext()) != null) {
                if (isHeader) { // 첫 번째 라인이 헤더인 경우 건너뜀
                    isHeader = false;
                    continue;
                }
                WorkoutDictionaryEntity workout = WorkoutDictionaryEntity.builder()
                        .workoutNameEnglish(values[0])
                        .workoutNameKorean(values[1])
                        .abbreviation(values[2].isEmpty() ? null : values[2])
                        .firstMeasurementUnit(MeasurementType.valueOf(values[3]))
                        .secondMeasurementUnit(values[4].isEmpty() ? null : MeasurementType.valueOf(values[4]))
                        .firstWorkoutCategory(WodCategoryType.valueOf(values[5]))
                        .secondWorkoutCategory(values[6].isEmpty() ? null : WodCategoryType.valueOf(values[6]))
                        .build();
                workouts.add(workout);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return workouts;
    }
}
