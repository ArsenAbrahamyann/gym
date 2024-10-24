package org.example.gym.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * Service that provides custom metrics for the application using Micrometer.
 * This service tracks the total number of HTTP requests, response times, and
 * the number of active users.
 */
@Service
@Getter
public class MetricsService {
    private final Counter requestCounter;
    private final Timer responseTimeTimer;
    private int activeUsers;

    /**
     * Constructs a {@code MetricsService} and registers custom metrics
     * (such as request counts, response times, and active users) with the given
     * {@link MeterRegistry}.
     *
     * @param meterRegistry the registry where metrics will be registered
     */
    public MetricsService(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("http_requests_total")
                .description("Total number of HTTP requests")
                .register(meterRegistry);

        Gauge.builder("active_users", this, MetricsService::getActiveUsers)
                .description("Number of active users")
                .register(meterRegistry);

        this.responseTimeTimer = Timer.builder("http_response_time_seconds")
                .description("Response time of HTTP requests in seconds")
                .register(meterRegistry);
    }

    /**
     * Increments the total count of HTTP requests.
     */
    public void incrementRequestCounter() {
        requestCounter.increment();
    }

    /**
     * Increases the active users count when a user logs in.
     */
    public void userLoggedIn() {
        activeUsers++;
    }

    /**
     * Decreases the active users count when a user logs out.
     * Ensures that the active users count doesn't fall below zero.
     */
    public void userLoggedOut() {
        if (activeUsers
                > 0) {
            activeUsers--;
        }
    }

    /**
     * Records the response time for HTTP requests in milliseconds.
     *
     * @param duration the response time in milliseconds
     */
    public void recordResponseTime(long duration) {
        responseTimeTimer.record(duration, TimeUnit.MILLISECONDS);
    }


}
