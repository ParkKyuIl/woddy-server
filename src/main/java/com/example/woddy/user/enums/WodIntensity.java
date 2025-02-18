package com.example.woddy.user.enums;

import lombok.Getter;

@Getter
public enum WodIntensity {
    LOW(1, "Low intensity workout"),
    MODERATE(2, "Moderate intensity workout"),
    CHALLENGING(3, "Challenging workout"),
    HIGH(4, "High intensity workout"),
    EXTREME(5, "Extreme intensity workout");

    private final int level;
    private final String message;

    WodIntensity(int level, String message) {
        this.level = level;
        this.message = message;
    }

    public static WodIntensity fromLevel(int level) {
        for (WodIntensity intensityLevel : WodIntensity.values()) {
            if (intensityLevel.getLevel() == level) {
                return intensityLevel;
            }
        }
        throw new IllegalArgumentException("Invalid intensity level: " + level);
    }
}
