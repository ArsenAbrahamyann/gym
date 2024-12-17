package org.example.gym.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Service that provides custom metrics for the application using Micrometer.
 * This service tracks the total number of HTTP requests, response times, and
 * the number of active users.
 */
@Service
@Getter
@Setter
public class MetricsService {
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final Counter passwordChangeCounter;

    private int activeUsers = 0;
    private int inactiveUsers = 0;

    /**
     * Constructs a new {@code MetricsService} and initializes the counters and gauges for user metrics.
     *
     * @param meterRegistry the {@link MeterRegistry} used to register custom metrics.
     */
    public MetricsService(MeterRegistry meterRegistry) {
        this.loginSuccessCounter = Counter.builder("user_login_success_total")
                .description("Total number of successful logins")
                .register(meterRegistry);

        this.loginFailureCounter = Counter.builder("user_login_failure_total")
                .description("Total number of failed login attempts")
                .register(meterRegistry);

        this.passwordChangeCounter = Counter.builder("user_password_change_total")
                .description("Total number of password changes")
                .register(meterRegistry);

        Gauge.builder("active_users", this, MetricsService::getActiveUsers)
                .description("Number of active users")
                .register(meterRegistry);

        Gauge.builder("inactive_users", this, MetricsService::getInactiveUsers)
                .description("Number of inactive users")
                .register(meterRegistry);
    }

    /**
     * Records a successful login attempt by incrementing the {@code loginSuccessCounter} and
     * increasing the count of active users.
     */
    public void recordLoginSuccess() {
        loginSuccessCounter.increment();
        activeUsers++;
    }

    /**
     * Records a failed login attempt by incrementing the {@code loginFailureCounter}.
     */
    public void recordLoginFailure() {
        loginFailureCounter.increment();
    }

    /**
     * Records a password change event by incrementing the {@code passwordChangeCounter}.
     */
    public void recordPasswordChange() {
        passwordChangeCounter.increment();
    }

}
