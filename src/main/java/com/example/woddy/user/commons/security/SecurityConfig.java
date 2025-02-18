package com.example.woddy.user.commons.security;

import com.example.woddy.user.service.MyAuthenticationFailureHandler;
import com.example.woddy.user.service.CustomOAuth2UserService;
import com.example.woddy.user.utils.JwtAuthFilter;


import com.example.woddy.user.utils.JwtExceptionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] URL_TO_PERMIT = {
            "/h2-console/**", // H2 콘솔 경로 허용
            "/oauth2/**", // 인증 경로 허용
            "/login/**",
            "/api/push-notifications/**", // FCM 경로 허용
            "/swagger-ui/**", // Swagger 경로 허용
            "/api-docs/**",
            "/error"
    };


    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private MyAuthenticationFailureHandler oAuth2LoginFailureHandler;


    @Autowired
    private CustomOAuth2UserService OAuth2CustomUserService;

    @Autowired
    private JwtAuthFilter JwtAuthFilter;

    @Autowired
    private JwtExceptionFilter JwtExceptionFilter;




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP 기본 인증 비활성화
                .cors(withDefaults())                        // CORS 설정
                .csrf(AbstractHttpConfigurer::disable)       // CSRF 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 사용 안 함
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                              // 인증 없이 허용할 경로들
                                .requestMatchers(URL_TO_PERMIT).permitAll()
                                .anyRequest().authenticated() // 그 외의 요청은 모두 허용
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)).anonymous(AbstractHttpConfigurer::disable);

        // JWT 인증 필터 추가
        return http.addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(JwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }
}
