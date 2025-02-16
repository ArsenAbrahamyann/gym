package org.example.gym.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for producing JMS messages to update training workload details.
 */
@Service
@Slf4j
public class JmsProducerService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JmsProducerService with the specified JmsTemplate and ObjectMapper.
     *
     * @param jmsTemplate  the JMS template for sending messages
     * @param objectMapper the object mapper for JSON processing
     */

    @Autowired
    public JmsProducerService(@Lazy JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a JMS message containing the update of a trainer workload.
     *
     * @param request the data transfer object containing the workload details
     */
    public void sendTrainingUpdate(TrainerWorkloadRequestDto request) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(request);

            Message<String> message = MessageBuilder
                    .withPayload(jsonMessage)
                    .setHeader("contentType", "application/json")
                    .build();

            jmsTemplate.convertAndSend("trainer.training.update", jsonMessage);
            log.info("Message sent successfully to trainer.training.update: {}", message);
        } catch (JmsException e) {
            log.error("Failed to send message to trainer.training.update. Sending to DLQ", e);

            boolean retrySuccess = retrySend("trainer.training.update", request, 3);
            if (!retrySuccess) {
                String errorMessage = prepareErrorMessage(request, e);
                jmsTemplate.convertAndSend("trainer.training.dlq", errorMessage);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send message to" + e.getMessage());
        }
    }

    /**
     * Attempts to resend a message to a specified queue up to a maximum number of retries.
     *
     * @param queue       the queue name to send the message to
     * @param request     the message content
     * @param maxRetries  the number of times to retry sending the message
     * @return true if the message was successfully sent within the retry limit; false otherwise
     */
    private boolean retrySend(String queue, TrainerWorkloadRequestDto request, Integer maxRetries) {
        for (Integer i = 0; i < maxRetries; i++) {
            try {
                Thread.sleep(1000);
                jmsTemplate.convertAndSend(queue, request);
                log.info("Retry {} successful for queue {}", i + 1, queue);
                return true;
            } catch (Exception ex) {
                log.warn("Retry {} failed for queue {}", i + 1, queue, ex);
            }
        }
        return false;
    }

    /**
     * Prepares a structured error message to send to the dead-letter queue (DLQ).
     *
     * @param originalMessage the original message that failed to be sent
     * @param e the exception that caused the send failure
     * @return the structured error message as a JSON string
     */
    private String prepareErrorMessage(TrainerWorkloadRequestDto originalMessage, Exception e) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "originalMessage", originalMessage,
                    "error", e.getClass().getSimpleName() + ": " + e.getMessage(),
                    "timestamp", System.currentTimeMillis(),
                    "retryCount", 3
            ));
        } catch (Exception jsonException) {
            return "Error converting message to JSON: " + originalMessage;
        }
    }
}
