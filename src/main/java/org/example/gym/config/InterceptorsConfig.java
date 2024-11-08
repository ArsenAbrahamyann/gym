package org.example.gym.config;

import java.security.SecureRandom;
import org.example.gym.logger.TransactionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class that registers interceptors for handling web requests.
 * This class implements {@link WebMvcConfigurer} and is responsible for adding
 * custom interceptors to the application's request handling flow.
 * for logging and monitoring transactions.
 */
@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

//    @Autowired
//    private AuthInterceptor authInterceptor;

//    @Autowired
//    private TransactionInterceptor transactionInterceptor;
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/trainee/*", "/trainer/*", "/training/*", "/user/*", "/trainingType/*")
//                .excludePathPatterns("/trainee/registration", "/trainer/registration", "/custom/metrics");
//
//        registry.addInterceptor(transactionInterceptor)
//                .addPathPatterns("/**");
//
//    }

    @Bean
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
}
