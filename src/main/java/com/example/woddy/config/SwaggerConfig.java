package com.example.woddy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Woddy API",
                description = "Woddy에서 사용하는 API를 설명 및 테스트하는 페이지 입니다 😊<br>" +
                        "DevTeam: Zenmasi \uD83C\uDF5C <br>" +
                        "Developers: Frank, Hukudome, Hakim <br> <br>" +
                        "**우측의 초록색 Authorize에 JWT을 세팅하셔야 합니다!**",
                version = "v1.0"
        )
)
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new io.swagger.v3.oas.models.servers.Server()
                        .url(serverUrl)
                        .description("Woddy API Server"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    public GroupedOpenApi fcmOpenApi() {
        return GroupedOpenApi.builder()
                .group("FCM API")
                .pathsToMatch("/api/push-notifications/**")
                .build();
    }

    @Bean
    public GroupedOpenApi oauthOpenApi() {
        return GroupedOpenApi.builder()
                .group("OAuth API")
                .pathsToMatch("/oauth2/**")
                .build();
    }
    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder()
                .group("WOD API")
                .pathsToMatch("/api/wod/**")
                .build();
    }

}


