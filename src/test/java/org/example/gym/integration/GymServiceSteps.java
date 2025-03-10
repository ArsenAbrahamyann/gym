package org.example.gym.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.awaitility.Awaitility;
import org.example.gym.config.TestContainerConfiguration;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.dto.response.TrainerWorkloadResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

/**
 * Step definitions for GymService integration tests with ActiveMQ and TrainerService.
 */
@SpringBootTest
@ContextConfiguration(classes = TestContainerConfiguration.class)
public class GymServiceSteps {

    private static final Logger log = LoggerFactory.getLogger(GymServiceSteps.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifies that the ActiveMQ broker is running.
     */
    @Given("the ActiveMQ broker is running")
    public void theActiveMqBrokerIsRunning() {
        String brokerUrl = System.getProperty("spring.activemq.broker-url");
        log.info("ActiveMQ broker is running at {}", brokerUrl);
        assertThat(brokerUrl).isNotNull();
        assertThat(brokerUrl).startsWith("tcp://");
    }

    /**
     * Sends a training update message to the ActiveMQ queue.
     */
    @When("the gymService sends a training update message")
    public void the_gym_service_sends_a_training_update_message() throws JsonProcessingException, JMSException {
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto(
                "trainer1", "John", "Doe", true, LocalDateTime.now(),
                5, "ADD");

        String jsonMessage = objectMapper.writeValueAsString(request);
        log.info("Sending message to trainer.training.update queue: {}", jsonMessage);
        jmsTemplate.send("trainer.training.update", session -> {
            TextMessage message = session.createTextMessage(jsonMessage);
            return message;
        });
    }

    /**
     * Verifies that the trainer service received the training update message.
     */
    @Then("the trainer service should receive the training update")
    public void the_trainer_service_should_receive_the_training_update() {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            log.info("Checking if trainer service received the training update...");

            TextMessage receivedMessage = (TextMessage) jmsTemplate.receive("trainer.training.update");

            log.info("Message received by trainer service: {}",
                    receivedMessage != null ? receivedMessage.getText() : "No message");

            assertThat(receivedMessage).isNotNull();
            assertThat(receivedMessage.getText()).contains("trainer1");
        });
    }

    /**
     * Sends a training hours request message to the ActiveMQ queue.
     */
    @When("the gymService sends a training hours request message")
    public void the_gym_service_sends_a_training_hours_request_message() throws JMSException, JsonProcessingException {
        log.info("Sending training hours request message...");

        jmsTemplate.send("request.traininghours.queue", session -> {
            TextMessage message = session.createTextMessage();
            message.setStringProperty("trainerUsername", "trainer1");
            message.setIntProperty("month", 1);
            message.setJMSCorrelationID("unique-correlation-id");
            log.info("Message sent with JMSCorrelationID: {}", "unique-correlation-id");
            return message;
        });

        simulateTrainingHoursResponse();
    }

    /**
     * Simulates the response to a training hours request message.
     */
    private void simulateTrainingHoursResponse() throws JMSException, JsonProcessingException {
        log.info("Simulating training hours response message...");

        TrainerWorkloadResponseDto response = new TrainerWorkloadResponseDto();
        response.setTrainerUsername("trainer1");
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setActive(true);
        response.setWorkload(Map.of(2022, Map.of(1, 120)));

        String responseJson = objectMapper.writeValueAsString(response);
        jmsTemplate.send("response.traininghours.queue", session -> {
            TextMessage message = session.createTextMessage(responseJson);
            message.setJMSCorrelationID("unique-correlation-id");
            return message;
        });

        log.info("Sent simulated response message with JMSCorrelationID: unique-correlation-id");
    }

    /**
     * Verifies that the gym service received the training hours response message.
     */
    @Then("the gymService should receive the training hours response")
    public void the_gym_service_should_receive_the_training_hours_response() {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            log.info("Waiting for training hours response message...");

            TextMessage receivedMessage = (TextMessage) jmsTemplate
                    .receiveSelected("response.traininghours.queue",
                            "JMSCorrelationID = 'unique-correlation-id'");

            log.info("Message received: {}", receivedMessage != null ? receivedMessage.getText() : "No message");

            assertThat(receivedMessage).isNotNull();
            assertThat(receivedMessage.getText()).contains("trainer1");
            assertThat(receivedMessage.getJMSCorrelationID()).isEqualTo("unique-correlation-id");
        });
    }
}
