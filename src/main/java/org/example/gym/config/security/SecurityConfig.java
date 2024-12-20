package org.example.gym.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for setting up authentication, authorization, and JWT-based security.
 *
 * <p>This configuration class enables web security and configures various aspects of Spring Security,
 * such as HTTP security, session management, CORS, and JWT token handling. It also defines custom
 * authentication mechanisms, password encoding, and an authentication manager.</p>
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final CustomLoginFilter customLoginFilter;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final JwtUtils jwtUtils;

    /**
     * Constructs a SecurityConfig object to initialize the necessary services for security configuration.
     *
     * @param userDetailsService the custom user details service
     * @param entryPoint the JWT authentication entry point
     * @param customLoginFilter the custom login filter for authentication
     * @param customLogoutHandler the custom logout handler for handling logout operations
     * @param jwtUtils utility class for handling JWT operations
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint entryPoint,
                          @Lazy CustomLoginFilter customLoginFilter,
                          @Lazy CustomLogoutHandler customLogoutHandler,
                          CustomLogoutSuccessHandler customLogoutSuccessHandler,
                          JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.entryPoint = entryPoint;
        this.customLoginFilter = customLoginFilter;
        this.customLogoutHandler = customLogoutHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.jwtUtils = jwtUtils;
    }

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
                .cors(Customizer.withDefaults())
                .csrf(customizer -> customizer.disable())
                .oauth2ResourceServer(configurer -> configurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/trainer/registration",
                                "/user/login",
                                "/trainee/registration",
                                "/actuator/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/trainee/**", "/trainer/**")
                        .hasAnyRole("TRAINER", "TRAINEE")
                        .requestMatchers("/trainer/**")
                        .hasRole("TRAINER")
                        .requestMatchers("/trainee/**")
                        .hasRole("TRAINEE")
                        .anyRequest().authenticated())
                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint))
                .exceptionHandling(Customizer.withDefaults())
                .build();


    }

    /**
     * Creates a JwtAuthenticationConverter for converting JWT tokens into authentication objects.
     *
     * <p>This converter extracts roles from the JWT and maps them to {@link GrantedAuthority} objects.
     * It also checks if the token is revoked using the {@link JwtUtils#isTokenRevoked} method.</p>
     *
     * @return the configured JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<String> roles = jwt.getClaimAsStringList(SecurityConstants.ROLES);
            log.debug("JWT roles: {}", roles);

            if (roles != null) {
                roles.forEach(role -> {
                    log.debug("Adding role: {}", role);
                    authorities.add(new SimpleGrantedAuthority(role));
                });
            }
            return authorities;
        });
        return converter;
    }


    /**
     * Configures a JwtDecoder for decoding JWT tokens using a secret key.
     *
     * @return the configured JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder build = NimbusJwtDecoder.withSecretKey(jwtUtils.getKey()).build();
        return new CustomJwtDecoder(build, jwtUtils);
    }

    /**
     * Configures a logoutHandler for logout.
     *
     * @return the SecurityContextLogoutHandler
     */
    @Bean
    public LogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    /**
     * Configures a CORS configuration source for handling cross-origin requests.
     *
     * <p>This method defines allowed origins, methods, and headers for CORS requests.</p>
     *
     * @return the configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8082"));
        corsConfiguration.setAllowedMethods(List.of("OPTIONS", "GET", "PUT", "PATCH", "POST", "HEAD", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of(SecurityConstants.HEADER_STRING));
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return configurationSource;
    }



    /**
     * Defines the authentication provider that Spring Security uses for authenticating users.
     *
     * <p>This provider uses {@link BCryptPasswordEncoder} for password encoding and a custom
     * {@link CustomUserDetailsService} to load user details from a data source.</p>
     *
     * @return the configured AuthenticationProvider bean
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
     * @param authenticationConfiguration the authentication configuration
     * @return the configured AuthenticationManager bean
     * @throws Exception if an error occurs while getting the authentication manager
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides the password encoder used for encoding and verifying passwords.
     *
     * <p>This method returns a {@link BCryptPasswordEncoder} that uses a strength of 12 for hashing passwords.</p>
     *
     * @return the configured BCryptPasswordEncoder bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


