package com.example.woddy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private String accessToken;   // JWT 액세스 토큰
    private Long duration;        // 액세스 토큰의 만료 시간
}
