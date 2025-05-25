package com.example.travelappv2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI travelAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TravelApp API")
                        .description("API REST pour l'application TravelApp avec support pour le chatbot Groq")
                        .version("1.0"));
    }
}
