package edu.gcu.cst339.lab2_chinook_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Spring configuration for the Swagger/OpenAPI documentation.
 *
 * <p>springdoc-openapi scans the controllers to build the API description;
 * this class supplies the title, description and version shown at the top of
 * the Swagger UI page ({@code /swagger-ui.html}) and in the JSON description
 * at {@code /v3/api-docs}.</p>
 */
@Configuration
class OpenApiConfig {

    /**
     * Defines the general information block of the OpenAPI document.
     *
     * @return the OpenAPI definition containing the API title, description and version
     */
    @Bean
    OpenAPI chinookOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Chinook Album API")
                .description("CST-339 Lab 2 - CRUD API for Chinook albums using Spring Data JPA and PostgreSQL")
                .version("1.0"));
    }
}