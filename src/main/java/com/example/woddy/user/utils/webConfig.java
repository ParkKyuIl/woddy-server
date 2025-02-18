package com.example.woddy.user.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class webConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
