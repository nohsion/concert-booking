package com.sion.concertbooking.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.sion.concertbooking.domain.service.TokenProvider.CONCERT_TOKEN_HEADER;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                // X-Concert-Token 스키마
                                .addSecuritySchemes(CONCERT_TOKEN_HEADER, new SecurityScheme()
                                        .name(CONCERT_TOKEN_HEADER)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                )
                );
    }
}