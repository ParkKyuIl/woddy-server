package com.example.woddy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDTO {
    private String accessToken;   // JWT 액세스 토큰
}

