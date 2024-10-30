package org.example.gym.actuator;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator that monitors the health of various system resources,
 * including database connection, disk space, CPU usage, and thread count.
 * This component is used by Spring Boot Actuator to report health status.
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {
    private final JdbcTemplate jdbcTemplate;
    private static final long DISK_SPACE_THRESHOLD = 1000000000L;
    private static final double CPU_USAGE_THRESHOLD = 0.9;
    private static final int THREAD_COUNT_THRESHOLD = 1000;

    /**
     * Constructs a CustomHealthIndicator with a provided JdbcTemplate for database health checks.
     *
     * @param jdbcTemplate the JdbcTemplate used to test the database connection.
     */
    @Autowired
    public CustomHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Checks the health of the application by combining various health indicators.
     * It reports the status of the database, disk space, CPU, and threads.
     *
     * @return the overall health status (UP if all checks pass, DOWN otherwise).
     */
    @Override
    public Health health() {
        Health databaseHealth = checkDatabaseHealth();
        Health diskSpaceHealth = checkDiskSpaceHealth();
        Health cpuHealth = checkCpuHealth();
        Health threadHealth = checkThreadHealth();

        if (databaseHealth.getStatus() == Health.up().build().getStatus()
                &&
                diskSpaceHealth.getStatus() == Health.up().build().getStatus()
                &&
                cpuHealth.getStatus() == Health.up().build().getStatus()
                &&
                threadHealth.getStatus() == Health.up().build().getStatus()) {
            return Health.up()
                    .withDetail("database", databaseHealth.getDetails())
                    .withDetail("diskSpace", diskSpaceHealth.getDetails())
                    .withDetail("cpu", cpuHealth.getDetails())
                    .withDetail("threads", threadHealth.getDetails())
                    .build();
        } else {
            return Health.down()
                    .withDetail("database", databaseHealth.getDetails())
                    .withDetail("diskSpace", diskSpaceHealth.getDetails())
                    .withDetail("cpu", cpuHealth.getDetails())
                    .withDetail("threads", threadHealth.getDetails())
                    .build();
        }
    }

    /**
     * Checks the health of the database by attempting a simple query.
     *
     * @return the health status of the database (UP if successful, DOWN otherwise).
     */
    private Health checkDatabaseHealth() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up()
                    .withDetail("Database", "Connection successful")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("Database", "Connection failed: "
                            + e.getMessage())
                    .build();
        }
    }

    /**
     * Checks the available disk space on the system.
     *
     * @return the health status of the disk space (UP if sufficient, DOWN if below threshold).
     */
    private Health checkDiskSpaceHealth() {
        File disk = new File("/");
        long freeSpace = disk.getFreeSpace();

        if (freeSpace >= DISK_SPACE_THRESHOLD) {
            return Health.up()
                    .withDetail("Disk space", "Sufficient space available")
                    .withDetail("Free space (bytes)", freeSpace)
                    .build();
        } else {
            return Health.down()
                    .withDetail("Disk space", "Low disk space")
                    .withDetail("Free space (bytes)", freeSpace)
                    .build();
        }
    }

    /**
     * Checks the CPU usage of the system.
     *
     * @return the health status of the CPU (UP if usage is normal, DOWN if above threshold).
     */
    private Health checkCpuHealth() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            double cpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getSystemCpuLoad();
            if (cpuLoad >= 0 && cpuLoad < CPU_USAGE_THRESHOLD) {
                return Health.up()
                        .withDetail("CPU Usage", "Normal")
                        .withDetail("CPU Usage (%)", cpuLoad
                                * 100)
                        .build();
            } else {
                return Health.down()
                        .withDetail("CPU Usage", "High CPU usage")
                        .withDetail("CPU Usage (%)", cpuLoad
                                * 100)
                        .build();
            }
        }
        return Health.unknown().withDetail("CPU", "Unable to determine CPU usage").build();
    }

    /**
     * Checks the number of active threads on the system.
     *
     * @return the health status of the thread count (UP if within limits, DOWN if above threshold).
     */
    private Health checkThreadHealth() {
        ThreadMXBean threadmxBean = ManagementFactory.getThreadMXBean();
        int threadCount = threadmxBean.getThreadCount();

        if (threadCount < THREAD_COUNT_THRESHOLD) {
            return Health.up()
                    .withDetail("Thread count", "Normal")
                    .withDetail("Active threads", threadCount)
                    .build();
        } else {
            return Health.down()
                    .withDetail("Thread count", "Too many active threads")
                    .withDetail("Active threads", threadCount)
                    .build();
        }
    }
}
