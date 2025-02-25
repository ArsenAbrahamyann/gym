package org.example.gym.componentTesting.steps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import org.example.gym.dto.request.TraineeRegistrationRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.mapper.TraineeMapper;
import org.example.gym.utils.UserUtils;

public class TraineeMapperSteps {

    private TraineeRegistrationRequestDto requestDto;
    private TraineeEntity resultEntity;
    private Exception thrownException;
    private TraineeMapper traineeMapper;

    @Before
    public void setUp() {
        UserUtils userUtils = mock(UserUtils.class);
        when(userUtils.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userUtils.generatePassword()).thenReturn("strongpassword");
        traineeMapper = new TraineeMapper(userUtils);
    }

    @Given("a valid TraineeRegistrationRequestDto")
    public void a_valid_TraineeRegistrationRequestDto() {
        requestDto = new TraineeRegistrationRequestDto("John", "Doe",  LocalDateTime.now(), "Address 123");
    }

    @When("the request is mapped to a TraineeEntity")
    public void the_request_is_mapped_to_a_TraineeEntity() {
        try {
            resultEntity = traineeMapper.traineeRegistrationMapToEntity(requestDto);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the TraineeEntity should have correct values")
    public void the_TraineeEntity_should_have_correct_values() {
        assertNotNull(resultEntity);
        assertEquals("John", resultEntity.getUser().getFirstName());
    }

    @Given("an invalid TraineeRegistrationRequestDto")
    public void anInvalidTraineeRegistrationRequestDto() {
        requestDto = new TraineeRegistrationRequestDto();
        requestDto.setFirsName(null);
        requestDto.setLastName("");
        requestDto.setDateOfBrith(null);
    }
    @Then("an exception should be thrown")
    public void anExceptionShouldBeThrown() {
        assertNotNull("Expected an exception to be thrown due to invalid DTO",
                (Supplier<String>) thrownException);
        assertTrue(!(thrownException instanceof IllegalArgumentException),
                "Expected exception to be of type IllegalArgumentException");
    }
}
