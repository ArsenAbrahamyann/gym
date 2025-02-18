package org.example.gym.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.jms.TextMessage;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.dto.response.TrainerWorkloadResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;

@ExtendWith(MockitoExtension.class)
public class JmsProducerServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RetryTemplate retryTemplate;

    @InjectMocks
    private JmsProducerService jmsProducerService;

    @BeforeEach
    void setUp() {
        jmsProducerService = new JmsProducerService(jmsTemplate, objectMapper, retryTemplate);
    }

    @Test
    void testSendTrainingUpdate_Success() throws JsonProcessingException {
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto();
        when(objectMapper.writeValueAsString(request)).thenReturn("{}");
        jmsProducerService.sendTrainingUpdate(request);

        verify(jmsTemplate, times(1)).convertAndSend(eq("trainer.training.update"),
                anyString());
    }

    @Test
    void testSendTrainingUpdate_ExceptionHandled() throws JsonProcessingException {
        TrainerWorkloadRequestDto request = new TrainerWorkloadRequestDto();
        when(objectMapper.writeValueAsString(request)).thenThrow(new JsonProcessingException("Error") {});

        jmsProducerService.sendTrainingUpdate(request);

        verify(jmsTemplate, times(1)).convertAndSend(eq("trainer.training.update.dlq"),
                anyString());
    }


    @Test
    void testRequestTrainingHours_Timeout() {
        String trainerUsername = "john_doe";
        Integer month = 8;

        TrainerWorkloadResponseDto result = jmsProducerService.requestTrainingHours(trainerUsername, month);

        assertNull(result);
    }

    @Test
    void testHandleTrainingHoursResponse_Success() throws Exception {
        String correlationId = UUID.randomUUID().toString();
        TextMessage message = mock(TextMessage.class);
        TrainerWorkloadResponseDto responseDto = new TrainerWorkloadResponseDto();
        CompletableFuture<TrainerWorkloadResponseDto> future = new CompletableFuture<>();
        jmsProducerService.pendingRequests.put(correlationId, future);

        when(message.getJMSCorrelationID()).thenReturn(correlationId);
        when(message.getText()).thenReturn("{}");
        when(objectMapper.readValue("{}", TrainerWorkloadResponseDto.class)).thenReturn(responseDto);

        jmsProducerService.handleTrainingHoursResponse(message);

        assertTrue(future.isDone());
        assertEquals(responseDto, future.get());
    }

}
