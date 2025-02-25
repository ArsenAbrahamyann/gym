package org.example.gym.integrationTesting;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import javax.jms.TextMessage;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.dto.response.TrainerWorkloadResponseDto;
import org.example.gym.service.JmsProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;

@EnableJms
@SpringBootTest
@DirtiesContext
public class JmsProducerServiceIntegrationTest {

    @Autowired
    private JmsProducerService jmsProducerService;

    @Autowired
    private JmsTemplate jmsTemplate;



    @Test
    public void testSendTrainingUpdate() throws Exception {
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto(
                "trainer1", "John", "Doe", true,
                LocalDateTime.now(), 154, "ADD");

        jmsProducerService.sendTrainingUpdate(request);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            TextMessage receivedMessage = (TextMessage) jmsTemplate.receive("trainer.training.update");
            assertEquals("trainer1", receivedMessage.getText()
                    .split("trainerUsername")[1].split(",")[0].trim());
        });
    }

    @Test
    public void testRequestTrainingHours() {
        jmsTemplate.send("response.traininghours.queue", session -> {
            TextMessage message = session.createTextMessage(
                    "{\"trainerUsername\": \"trainer1\", \"workload\": {}}");
            message.setJMSCorrelationID("unique-correlation-id");
            return message;
        });

        TrainerWorkloadResponseDto response = jmsProducerService
                .requestTrainingHours("trainer1", 1);

        assertEquals("trainer1", response.getTrainerUsername());
    }
}
