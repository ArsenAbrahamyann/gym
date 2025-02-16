package org.example.gym.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.dto.response.TrainerWorkloadResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for handling operations related to JMS messaging for training workloads.
 * This class manages sending workload updates and requests for training hours, along with receiving responses through JMS.
 */
@Service
@Slf4j
public class JmsProducerService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;
    private final Map<String, CompletableFuture<TrainerWorkloadResponseDto>> pendingRequests;

    /**
     * Constructs a new JmsProducerService with essential components for message handling.
     *
     * @param jmsTemplate  the JMS template for sending and receiving messages.
     * @param objectMapper the component for JSON processing.
     * @param retryTemplate the template for retrying operations upon failure.
     */
    @Autowired
    public JmsProducerService(@Lazy JmsTemplate jmsTemplate, ObjectMapper objectMapper, RetryTemplate retryTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.retryTemplate = retryTemplate;
        this.pendingRequests = new ConcurrentHashMap<>();
    }

    /**
     * Sends a JMS message with a training workload update encapsulated within the supplied request object.
     *
     * @param request the data transfer object containing trainer workload details, must not be null.
     * @throws JmsException if there is any issue with JMS operations.
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
        } catch (JmsException | JsonProcessingException e) {
            log.error("Failed to send message to trainer.training.update. Sending to DLQ", e);
            sendToDeadLetterQueue(request, e);
        }
    }



    /**
     * Sends a request to retrieve training hours for a specific trainer and month, and waits for a synchronous response.
     *
     * @param trainerUsername the username of the trainer, must not be null.
     * @param month the month for which training hours are requested, must not be null.
     * @return a {@link TrainerWorkloadResponseDto} containing the response, or null if no response is received within the set timeout.
     */
    public TrainerWorkloadResponseDto requestTrainingHours(String trainerUsername, Integer month) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<TrainerWorkloadResponseDto> futureResponse = new CompletableFuture<>();
        pendingRequests.put(correlationId, futureResponse);

        Map<String, Object> requestPayload = Map.of(
                "trainerUsername", trainerUsername,
                "month", month,
                "correlationId", correlationId
        );
        try {
            retryTemplate.execute(context -> {
                jmsTemplate.convertAndSend("request.traininghours.queue", requestPayload, message -> {
                    message.setJMSCorrelationID(correlationId);
                    return message;
                });
                log.info("Sent training hours request for {} (month: {}) with correlationId = {}",
                        trainerUsername, month, correlationId);
                return null;
            });

            return futureResponse.get(10, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            log.error("Timeout waiting for response for correlationId = {}", correlationId);
            sendToDeadLetterQueuee(requestPayload, "Timeout waiting for response.");
            return null;
        } catch (Exception e) {
            log.error("Error while sending training hours request", e);
            sendToDeadLetterQueuee(requestPayload, "Error while sending request: " + e.getMessage());
            return null;
        } finally {
            pendingRequests.remove(correlationId);
        }
    }

    /**
     * Listener for handling incoming JMS messages containing responses for training hours requests.
     *
     * @param message the received JMS message, expected to be of type {@link TextMessage}.
     */
    @JmsListener(destination = "response.traininghours.queue")
    public void handleTrainingHoursResponse(javax.jms.Message message) {
        try {
            if (message instanceof TextMessage) {
                String correlationId = message.getJMSCorrelationID();
                String payload = ((TextMessage) message).getText();
                log.info("Received response with correlationId = {}: {}", correlationId, payload);

                TrainerWorkloadResponseDto responseDto = objectMapper.readValue(payload,
                        TrainerWorkloadResponseDto.class);

                CompletableFuture<TrainerWorkloadResponseDto> future = pendingRequests.remove(correlationId);
                if (future != null) {
                    future.complete(responseDto);
                }
            }
        } catch (Exception e) {
            log.error("Error processing response message.", e);
        }
    }

    /**
     * Sends the original message along with a description of the error to the Dead Letter Queue (DLQ) for later analysis or reprocessing.
     *
     * @param originalMessage the original message that failed to process.
     * @param errorReason a description of why processing failed.
     */
    private void sendToDeadLetterQueuee(Map<String, Object> originalMessage, String errorReason) {
        try {
            Map<String, Object> dlqPayload = new HashMap<>(originalMessage);
            dlqPayload.put("errorReason", errorReason);
            dlqPayload.put("timestamp", System.currentTimeMillis());

            String jsonMessage = objectMapper.writeValueAsString(dlqPayload);
            jmsTemplate.convertAndSend("deadLetter.traininghours.queue", jsonMessage);
            log.warn("Sent message to DLQ: {}", jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize DLQ message.", e);
        }
    }

    /**
     * Overloaded method to send failed messages directly to the DLQ.
     *
     * @param request the originating request that resulted in failure.
     * @param exception the exception that occurred during processing.
     */
    private void sendToDeadLetterQueue(TrainerWorkloadRequestDto request, Exception exception) {
        try {
            String errorDetails = "Failed message: " + request.toString() + "\nError: " + exception.getMessage();
            jmsTemplate.convertAndSend("trainer.training.update.dlq", errorDetails);
            log.info("Failed message sent to DLQ");
        } catch (Exception e) {
            log.error("Failed to send message to DLQ", e);
        }
    }
}
