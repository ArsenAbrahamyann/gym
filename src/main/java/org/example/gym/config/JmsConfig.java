package org.example.gym.config;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class for setting up JMS related components.
 * Utilizes ActiveMQ as the message broker.
 */
@Configuration
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    String BROKER_URL;
    @Value("${spring.activemq.user}")
    String BROKER_USERNAME;
    @Value("${spring.activemq.password}")
    String BROKER_PASSWORD;

    /**
     * Creates and configures a {@link ConnectionFactory} using ActiveMQ settings.
     *
     * @return a configured instance of {@link ActiveMQConnectionFactory}
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    /**
     * Defines a {@link JmsTemplate} bean with the specified {@link ConnectionFactory}.
     * JmsTemplate is used for producing and consuming messages.
     *
     * @param connectionFactory The connection factory used to create connections to the broker.
     * @return a {@link JmsTemplate} initialized with the specified connection factory.
     */
    @Bean
    public JmsTemplate getJmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }


}
