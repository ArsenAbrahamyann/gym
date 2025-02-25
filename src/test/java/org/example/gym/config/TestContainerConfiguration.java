package org.example.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.testcontainers.containers.GenericContainer;

/**
 * Configuration class for setting up the ActiveMQ broker using Testcontainers
 * and configuring the necessary JMS components.
 */
@Configuration
@EnableJms
public class TestContainerConfiguration {
    static GenericContainer<?> activeMq = new GenericContainer<>("rmohr/activemq:latest")
            .withExposedPorts(61616);

    static {
        activeMq.start();

        System.setProperty("spring.activemq.broker-url", "tcp://" + activeMq.getHost() + ":"
                + activeMq.getMappedPort(61616));
        System.setProperty("ACTIVEMQ_BROKER_URL", System.getProperty("spring.activemq.broker-url"));
        System.setProperty("ACTIVEMQ_USERNAME", "admin");
        System.setProperty("ACTIVEMQ_PASSWORD", "admin");
    }

    /**
     * Creates a ConnectionFactory bean for connecting to the ActiveMQ broker.
     *
     * @return the ConnectionFactory for ActiveMQ
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(System.getProperty("spring.activemq.broker-url"));
    }

    /**
     * Creates a JmsTemplate bean for sending and receiving JMS messages.
     *
     * @param connectionFactory the ConnectionFactory for ActiveMQ
     * @return the JmsTemplate configured for ActiveMQ
     */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    /**
     * Creates an ObjectMapper bean configured with JavaTimeModule for handling
     * Java 8 date and time types and to disable writing dates as timestamps.
     *
     * @return the configured ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
