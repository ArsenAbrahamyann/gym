package org.example.gym.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

@ExtendWith(MockitoExtension.class)
public class JmsProducerServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JmsProducerService jmsProducerService;

    private static final String QUEUE_NAME = "trainer.training.update";
    private TrainerWorkloadRequestDto request;
    private final String jsonMessage = "{\"username\":\"john.doe\"}";

    /**
     * Sets up the necessary preconditions before each test execution.
     * Initializes request objects and configures mock behavior.
     *
     * @throws Exception if there is any issue during the setup.
     */
    @BeforeEach
    public void setUp() throws Exception {
        request = new TrainerWorkloadRequestDto();
        request.setTrainerUsername("john.doe");

        when(objectMapper.writeValueAsString(any(TrainerWorkloadRequestDto.class))).thenReturn(jsonMessage);
    }

    @Test
    public void shouldSendTrainingUpdateSuccessfully() {
        jmsProducerService.sendTrainingUpdate(request);
        verify(jmsTemplate).convertAndSend(eq(QUEUE_NAME), eq(jsonMessage));
        verifyNoMoreInteractions(jmsTemplate);
    }

    @Test
    public void shouldHandleJmsExceptionWhenSendingTrainingUpdate() {
        doThrow(new JmsException("JMS failure") {}).when(jmsTemplate).convertAndSend(eq(QUEUE_NAME), eq(jsonMessage));

        jmsProducerService.sendTrainingUpdate(request);

        // Verify that an attempt was made to send a message
        verify(jmsTemplate).convertAndSend(eq(QUEUE_NAME), eq(jsonMessage));
        // Add verification for logging or error handling if applicable
    }
}
