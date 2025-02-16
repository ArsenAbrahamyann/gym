package org.example.gym.config;

import javax.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * Configuration class for setting up JMS related components.
 * Utilizes ActiveMQ as the message broker.
 */
@Configuration
@Slf4j
public class JmsConfig {

    String brokerUrl = System.getenv("ACTIVEMQ_BROKER_URL");
    String brokerUsername = System.getenv("ACTIVEMQ_USERNAME");
    String brokerPassword = System.getenv("ACTIVEMQ_PASSWORD");

    /**
     * Creates and configures a {@link ConnectionFactory} using ActiveMQ settings.
     *
     * @return a configured instance of {@link ActiveMQConnectionFactory}
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setPassword(brokerUsername);
        connectionFactory.setUserName(brokerPassword);
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

    /**
     * Creates and configures a {@link DefaultJmsListenerContainerFactory} that will be used to create JMS Listener Containers.
     * The factory is set up with a connection factory and an error handler that logs to the standard error stream upon any error.
     * Note: This configuration is intended for Point-to-Point messaging model as indicated by 'setPubSubDomain' set to 'false'.
     *
     * @return the listener container factory configured for standard JMS queues.
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("5-10");  // Horizontal scaling
        factory.setErrorHandler(t -> log.error("JMS Error", t));
        return factory;
    }
}
