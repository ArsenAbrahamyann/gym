package org.example.gym.actuator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling custom metrics-related endpoints.
 * This controller provides endpoints to log user activities (login, logout)
 * and track requests with response times.
 */
@RestController
@RequestMapping("/custom") // TODO metrics are not just a controller, the are configured in a different way
public class MetricsController {
    private final MetricsService metricsService;

    /**
     * Constructs a MetricsController with the specified MetricsService.
     *
     * @param metricsService the service used for tracking metrics such as login counts, logout counts,
     *                       request counters, and response times.
     */
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * Endpoint to simulate a user login action.
     * Increments the user login counter and the overall request counter.
     *
     * @return a message indicating that the user has logged in.
     */
    @GetMapping("/metrics/login")
    public String loginUser() {
        metricsService.userLoggedIn();
        metricsService.incrementRequestCounter();
        return "User logged in";
    }

    /**
     * Endpoint to simulate a user logout action.
     * Increments the user logout counter and the overall request counter.
     *
     * @return a message indicating that the user has logged out.
     */
    @GetMapping("/metrics/logout")
    public String logoutUser() {
        metricsService.userLoggedOut();
        metricsService.incrementRequestCounter();
        return "User logged out";
    }

    /**
     * Endpoint to simulate a request tracking action.
     * Increments the request counter and records the response time for the request.
     * The request is artificially delayed by a random time to simulate real-world processing.
     *
     * @return a message indicating that the request has been tracked.
     */
    @GetMapping("/metrics/track")
    public String trackRequest() {
        metricsService.incrementRequestCounter();

        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordResponseTime(duration);

        return "Request tracked";
    }
}
