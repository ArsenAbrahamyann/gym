package org.example.gym.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for setting up authentication, authorization, and JWT-based security.
 *
 * <p>This configuration class enables web security and configures various aspects of Spring Security,
 * such as HTTP security, session management, CORS, and JWT token handling. It also defines custom
 * authentication mechanisms, password encoding, and an authentication manager.</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p>This method sets up CORS, disables CSRF protection, configures URL-based authorization,
     * and adds a custom JWT filter for authentication. It also configures session management
     * to be stateless, meaning no session will be created or used by Spring Security.</p>
     *
     * @param http the HTTP security object used to configure the security filter chain
     * @return the configured security filter chain
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.addAllowedOriginPattern("*");
                    corsConfig.addAllowedMethod("*");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/trainer/registration",
                                "/trainee/registration",
                                "/user/login",
                                "/user/logout")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/trainee/**", "/trainer/**")
                        .hasAnyRole("TRAINER", "TRAINEE")
                        .requestMatchers("/trainer/**")
                        .hasRole("TRAINER")
                        .requestMatchers("/trainee/**")
                        .hasRole("TRAINEE")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }

    /**
     * Defines the authentication provider that Spring Security uses for authenticating users.
     *
     * <p>This provider uses {@link BCryptPasswordEncoder} for password encoding and a custom
     * {@link CustomUserDetailsService} to load user details from a data source.</p>
     *
     * @return the configured {@link AuthenticationProvider} bean
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    /**
     * Provides the authentication manager that is responsible for authenticating user credentials.
     *
     * <p>This bean is necessary for Spring Security's authentication mechanism.</p>
     *
     * @param configuration the authentication configuration
     * @return the configured {@link AuthenticationManager} bean
     * @throws Exception if an error occurs while getting the authentication manager
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Provides the password encoder used for encoding and verifying passwords.
     *
     * <p>This method returns a {@link BCryptPasswordEncoder} that uses a strength of 12 for hashing passwords.</p>
     *
     * @return the configured {@link BCryptPasswordEncoder} bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
