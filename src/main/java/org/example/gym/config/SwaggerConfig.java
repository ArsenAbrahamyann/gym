package org.example.gym.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.security.SecureRandom;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger config class.
 */
@Configuration
@Slf4j
public class SwaggerConfig {

    /**
     * Defines custom OpenAPI settings, including server URL and API information.
     *
     * @return a configured {@link OpenAPI} instance.
     */
    @Bean
    public OpenAPI myOpenApi() {
        log.debug("Configuring Custom OpenApi");

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080")
                ))
                .info(
                        new Info().title("Gym CRM")
                );

    }

    @Bean
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
}
