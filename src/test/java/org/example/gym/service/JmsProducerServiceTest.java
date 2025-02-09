package org.example.gym.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

@ExtendWith(MockitoExtension.class)
public class JmsProducerServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JmsProducerService jmsProducerService;

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
        when(objectMapper.writeValueAsString(request)).thenReturn(jsonMessage);
    }

    @Test
    public void testSendTrainingUpdate_Success() throws Exception {
        jmsProducerService.sendTrainingUpdate(request);
        verify(jmsTemplate).convertAndSend("trainer.training.update", jsonMessage);
    }

    @Test
    public void testSendTrainingUpdate_JsonProcessingException() throws Exception {
        when(objectMapper.writeValueAsString(request)).thenThrow(new RuntimeException("JSON processing failed"));

        try {
            jmsProducerService.sendTrainingUpdate(request);
        } catch (RuntimeException e) {
            verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
        }
    }
}
