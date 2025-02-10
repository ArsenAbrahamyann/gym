package org.example.gym.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetricsServiceTest {

    private MetricsService metricsService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metricsService = new MetricsService(meterRegistry);
    }

    @Test
    void testRecordLoginSuccess() {
        double before = metricsService.getLoginSuccessCounter().count();
        metricsService.recordLoginSuccess();
        double after = metricsService.getLoginSuccessCounter().count();

        assertThat(after).isEqualTo(before + 1);
        assertThat(metricsService.getActiveUsers()).isEqualTo(1);
    }

    @Test
    void testRecordLoginFailure() {
        double before = metricsService.getLoginFailureCounter().count();
        metricsService.recordLoginFailure();
        double after = metricsService.getLoginFailureCounter().count();

        assertThat(after).isEqualTo(before + 1);
    }

    @Test
    void testRecordPasswordChange() {
        double before = metricsService.getPasswordChangeCounter().count();
        metricsService.recordPasswordChange();
        double after = metricsService.getPasswordChangeCounter().count();

        assertThat(after).isEqualTo(before + 1);
    }

    @Test
    void testActiveUsersGauge() {
        metricsService.recordLoginSuccess();
        metricsService.recordLoginSuccess();

        assertThat(meterRegistry.find("active_users").gauge().value()).isEqualTo(2);
    }

    @Test
    void testInactiveUsersGauge() {
        metricsService.setInactiveUsers(5);

        assertThat(meterRegistry.find("inactive_users").gauge().value()).isEqualTo(5);
    }

}
