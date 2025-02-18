package com.example.woddy.fcm.controller;

import com.example.woddy.fcm.dto.FCMMessageDTO;
import com.example.woddy.fcm.service.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/push-notifications")
@Tag(name = "FCM", description = "Firebase Cloud Message API")
public class FCMController {
    private final FCMService fcmService;

    public FCMController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    @Operation(summary = "푸쉬 알림 전송", description = "FCM requestDTO 형식의 입력을 받아 사용자에게 푸쉬 알림을 전송합니다.")
    public ResponseEntity<String> sendNotification(@RequestBody FCMMessageDTO requestDTO) {
        String response = fcmService.sendNotification(requestDTO.getRecipientToken(), requestDTO.getTitle(), requestDTO.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

