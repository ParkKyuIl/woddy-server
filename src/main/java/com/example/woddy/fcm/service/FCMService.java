package com.example.woddy.fcm.service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FCMService {
    private static final Logger log = LoggerFactory.getLogger(FCMService.class);

    // Asynchronous sending of a single notification
    @Async
    public String sendNotification(String targetToken, String title, String message) {
        try {
            Message firebaseMessage = Message.builder()
                    .setToken(targetToken)
                    .putData("title", title)
                    .putData("message", message)
                    .build();

            String response = FirebaseMessaging.getInstance().send(firebaseMessage);
            return "Message sent successfully: " + response;
        } catch (Exception e) {
            log.error("Error sending notification to token {}: {}", targetToken, e.getMessage());
            return "Error sending message: " + e.getMessage();
        }
    }
}