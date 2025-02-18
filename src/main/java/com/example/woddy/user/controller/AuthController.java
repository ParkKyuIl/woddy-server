package com.example.woddy.user.controller;

import com.example.woddy.user.dto.CustomOAuth2User;
import com.example.woddy.user.service.CustomOAuth2UserService;
import com.example.woddy.user.dto.TokenResponseDTO;
import com.example.woddy.user.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    private final JwtTokenUtil tokenUtils;

    @Value("${jwt.expiration_time}")
    private Long expirationDate;

    @Autowired
    private CustomOAuth2UserService OAuth2CustomUserService;


    @Autowired
    public AuthController(JwtTokenUtil tokenUtils, CustomOAuth2UserService OAuth2CustomUserService) {
        this.tokenUtils = tokenUtils;
        this.OAuth2CustomUserService = OAuth2CustomUserService;
    }

    @Operation(
            summary = "JWT 토큰 발급",
            description = "구글의 idToken을 통해 사용자에게 JWT 토큰을 발급합니다."
    )
    @GetMapping("/oauth2/google")
    @ResponseBody
    public ResponseEntity<TokenResponseDTO> oauth2Google(@RequestParam("id_token") String idToken) throws JsonProcessingException {

        // 사용자 정보 가져오기
        CustomOAuth2User user = OAuth2CustomUserService.findOrSaveMember(idToken, "google");
        // 토큰 생성
        String token = tokenUtils.generateToken(user.getId(), expirationDate);

        // TokenResponseDTO 생성
        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
                .accessToken(token)
                .build();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type 설정

        // ResponseEntity 생성 및 반환
        return new ResponseEntity<>(tokenResponseDTO, headers, HttpStatus.OK);
    }

    @Operation(
        summary = "토큰 통해서 UserId 반환",
        description = "토큰을 통해서 사용자의 userId 정보를 반환합니다."
    )
    @GetMapping("/oauth2/userId")
    @ResponseBody
    public ResponseEntity<Long> getUserIdFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long userId = JwtTokenUtil.getUserIdFromToken(token);

            return ResponseEntity.ok(userId);
        }

        return ResponseEntity.status(401).body(-1L);
    }
}
