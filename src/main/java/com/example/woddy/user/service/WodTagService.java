package com.example.woddy.user.service;

import com.example.woddy.user.entity.WodEntity;
import com.example.woddy.user.entity.WorkoutDictionaryEntity;
import com.example.woddy.user.enums.TagType;
import com.example.woddy.user.enums.WodCategoryType;
import com.example.woddy.user.repository.WodDetailRepository;
import com.example.woddy.user.repository.WodRepository;
import com.example.woddy.user.repository.WorkoutDictionaryRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WodTagService {

  @Autowired
  private WodRepository wodRepository;
  @Autowired
  private WodDetailRepository wodDetailRepository;
  @Autowired
  private WorkoutDictionaryRepository workoutDictionaryRepository;

  private static final List<WodCategoryType> CATEGORIES = Arrays.asList(WodCategoryType.바벨, WodCategoryType.덤벨, WodCategoryType.짐내스틱, WodCategoryType.케틀벨, WodCategoryType.맨몸, WodCategoryType.줄넘기, WodCategoryType.유산소, WodCategoryType.기타, WodCategoryType.박스, WodCategoryType.샌드백);

  public TagType getUserTag(long userId) {
    List<WodEntity> userWods = wodRepository.findByUserId(userId);
    if (userWods.size() < 5) {
      return TagType.단식주의자;
    }

    Map<WodCategoryType, Long> categoryCount = userWods.stream()
        .flatMap(wod -> wodDetailRepository.findByWodId(wod.getId()).stream())
        .map(wodDetail -> workoutDictionaryRepository.findById(wodDetail.getWorkoutDictionaryEntity().getId())
            .orElseThrow(() -> new RuntimeException("Workout not found for ID: " + wodDetail.getWorkoutDictionaryEntity().getId()))
        )
        .collect(Collectors.groupingBy(
            WorkoutDictionaryEntity::getFirstWorkoutCategory,
            Collectors.counting()
        ));
    int totalWorkouts = categoryCount.values().stream().mapToInt(Long::intValue).sum();

    WodCategoryType dominantCategory = categoryCount.entrySet().stream()
        .max(Comparator.comparing(Map.Entry::getValue))
        .map(Map.Entry::getKey).orElse(WodCategoryType.기타);

    Map<WodCategoryType, Double> categoryRatios = calculateCategoryRatios(categoryCount, totalWorkouts);
    TagType tag = determineTagByCategory(dominantCategory, categoryRatios);

    return tag;
  }

  private Map<WodCategoryType, Double> calculateCategoryRatios(Map<WodCategoryType, Long> workoutData, int total) {
    Map<WodCategoryType, Double> ratios = new HashMap<>();
    for (WodCategoryType category : CATEGORIES) {
      long count = workoutData.getOrDefault(category, 0L);
      ratios.put(category, (double) count / total);
    }
    return ratios;
  }

  private TagType determineTagByCategory(WodCategoryType dominantCategory, Map<WodCategoryType, Double> ratios) {
    // 카테고리 그룹화
    Set<WodCategoryType> weightlifting = Set.of(WodCategoryType.바벨, WodCategoryType.덤벨, WodCategoryType.케틀벨, WodCategoryType.샌드백);
    Set<WodCategoryType> gymnastics = Set.of(WodCategoryType.맨몸, WodCategoryType.유산소, WodCategoryType.박스);
    Set<WodCategoryType> jumpRope = Set.of(WodCategoryType.줄넘기);

    // 육각형 먼저 체크
    if (isHexagonUser(ratios)) {
      return TagType.육각형;
    }

    // 가장 높은 카테고리 그룹별 체크
    if (jumpRope.contains(dominantCategory)) {
      return TagType.더블언더_권위자;
    }

    if (weightlifting.contains(dominantCategory)) {
      return TagType.역도원툴;
    }

    if (gymnastics.contains(dominantCategory)) {
      return TagType.체조선수;
    }

    return TagType.단식주의자;
  }

  private boolean isHexagonUser(Map<WodCategoryType, Double> ratios) {
    double threshold = 0.20; // 균등 비중 허용 오차
    List<Double> values = new ArrayList<>(ratios.values());
    double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    return values.stream().allMatch(v -> Math.abs(v - average) < threshold);
  }
}
