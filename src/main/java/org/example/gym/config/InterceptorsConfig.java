package org.example.gym.config;

import org.example.gym.auth.AuthInterceptor;
import org.example.gym.logger.TransactionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class that registers interceptors for handling web requests.
 * This class implements {@link WebMvcConfigurer} and is responsible for adding
 * custom interceptors to the application's request handling flow.
 * It registers the {@link AuthInterceptor} for authentication and the {@link TransactionInterceptor}
 * for logging and monitoring transactions.
 */
@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private TransactionInterceptor transactionInterceptor;

    /**
     * Adds interceptors to the registry for specific URL patterns.
     * - The {@link TransactionInterceptor} is applied to all incoming requests.
     * - The {@link AuthInterceptor} is applied to specific paths related to trainees, trainers, training, and users,
     * excluding paths for registration and metrics tracking.
     *
     * @param registry the {@link InterceptorRegistry} to which the interceptors are added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/trainee/*", "/trainer/*", "/training/*", "/user/*", "/trainingType/*")
                .excludePathPatterns("/trainee/registration", "/trainer/registration", "/custom/metrics");

        registry.addInterceptor(transactionInterceptor)
                .addPathPatterns("/**");

    }
}
