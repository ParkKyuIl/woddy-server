package com.example.woddy.fcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FCMMessageDTO {
    @Schema(description = "token for each device", example = "f5fMU0jTl0mruDCJvomaaV:APA91bFTJbRLL_ABIwHZanrwYgJZAi6vg3bwo7SQJnichpqvG4yWMQC77xuvMbL0QMncEfMn8H6NTFxRrxN1YN9IGmxmxW4OGs40g40jzONXOGvL6usxh5VVQv9FM6ZSRtpVGbb0U_87")
    private String recipientToken; // 각 사용자에게 보내는 FCM 토큰
    @Schema(description = "title for push notification", example = "운동을 시작하세요")
    private String title;
    @Schema(description = "message content for push notification", example = "오늘 하루도 힘내 봅시다")
    private String message;
}

