package edu.gcu.cst339.lab2_chinook_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI chinookOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Chinook Album API")
                .description("CST-339 Lab 2 - CRUD API for Chinook albums using Spring Data JPA and PostgreSQL")
                .version("1.0"));
    }
}