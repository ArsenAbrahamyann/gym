package org.example.gym.component.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.response.TrainingResponseDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.TrainingTypeEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.mapper.TrainingMapper;

/**
 * Cucumber steps for testing the {@link TrainingMapper}.
 */
public class TrainingMapperSteps {

    private TrainingMapper trainingMapper = new TrainingMapper();
    private List<TrainingEntity> trainingEntities;
    private List<TrainingResponseDto> trainingResponseDtos;
    private AddTrainingRequestDto requestDto;
    private TrainingEntity mappedTrainingEntity;
    private Exception exception;

    /**
     * Sets up a list of {@link TrainingEntity} objects for testing.
     */
    @Given("I have a list of TrainingEntity objects")
    public void have_a_list_of_TrainingEntity_objects() {
        trainingEntities = new ArrayList<>();
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setUsername("John");
        user.setIsActive(true);
        trainer.setUser(user);
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user1 = new UserEntity();
        user1.setUsername("Doe");
        user1.setIsActive(true);
        trainee.setUser(user1);
        TrainingEntity entity = new TrainingEntity();
        entity.setId(1L);
        entity.setTrainingName("Spin Class");
        entity.setTrainingDuration(60);
        entity.setTrainingDate(LocalDateTime.now());
        entity.setTrainee(trainee);
        entity.setTrainer(trainer);
        entity.setTrainingType(mock(TrainingTypeEntity.class));

        trainingEntities.add(entity);
    }

    /**
     * Sets up an empty list of {@link TrainingEntity} objects for testing.
     */
    @Given("I have an empty list of TrainingEntity objects")
    public void have_an_empty_list_of_TrainingEntity_objects() {
        trainingEntities = new ArrayList<>();
    }


    /**
     * Converts the list of {@link TrainingEntity} objects to a list of {@link TrainingResponseDto} objects.
     */
    @When("I convert them to TrainingResponseDto objects")
    public void convert_them_to_TrainingResponseDto_objects() {
        trainingResponseDtos = trainingMapper.mapToDtoTrainingTrainee(trainingEntities);
    }

    /**
     * Verifies that the conversion resulted in a list of {@link TrainingResponseDto} objects
     * matching the details of the original {@link TrainingEntity} objects.
     */
    @Then("I should receive a list of TrainingResponseDto objects matching the TrainingEntity objects details")
    public void should_receive_a_list_of_TrainingResponseDto_objects_matching_the_TrainingEntity_objects_details() {
        assertNotNull(trainingResponseDtos);
        assertEquals(1, trainingResponseDtos.size());
        assertEquals("Spin Class", trainingResponseDtos.get(0).getName());
    }

    /**
     * Verifies that the conversion of an empty list of {@link TrainingEntity} objects
     * resulted in an empty list of {@link TrainingResponseDto} objects.
     */
    @Then("I should receive an empty list of TrainingResponseDto objects")
    public void should_receive_an_empty_list_of_TrainingResponseDto_objects() {
        assertTrue(trainingResponseDtos.isEmpty());
    }


    /**
     * Sets up a valid {@link AddTrainingRequestDto} object for testing.
     */
    @Given("I have a valid AddTrainingRequestDto object")
    public void have_a_valid_AddTrainingRequestDto_object() {
        TraineeEntity trainee = mock(TraineeEntity.class);
        when(trainee.getId()).thenReturn(1L);

        TrainerEntity trainer = mock(TrainerEntity.class);
        when(trainer.getId()).thenReturn(1L);
        TrainingTypeEntity type = new TrainingTypeEntity(1L, "Yoga");

        when(trainer.getSpecialization()).thenReturn(type);

        requestDto = new AddTrainingRequestDto();
        requestDto.setTrainingName("Yoga Session");
        requestDto.setTrainingDuration(90);
        requestDto.setTrainingDate(LocalDateTime.now());

        mappedTrainingEntity = trainingMapper.requestDtoMapToTrainingEntity(requestDto, trainee, trainer);
    }

    /**
     * Converts the {@link AddTrainingRequestDto} object to a {@link TrainingEntity} object.
     */
    @When("I convert it to a TrainingEntity object")
    public void convert_it_to_a_TrainingEntity_object() {
        try {
            TraineeEntity trainee = mock(TraineeEntity.class);
            TrainerEntity trainer = mock(TrainerEntity.class);
            TrainingTypeEntity type = mock(TrainingTypeEntity.class);

            when(trainee.getId()).thenReturn(2L);
            when(trainer.getSpecialization()).thenReturn(type);

            mappedTrainingEntity = trainingMapper.requestDtoMapToTrainingEntity(requestDto, trainee, trainer);
        } catch (Exception e) {
            exception = e;
        }
    }

    /**
     * Verifies that the conversion resulted in a {@link TrainingEntity} object
     * matching the details of the original {@link AddTrainingRequestDto} object.
     */
    @Then("I should receive a TrainingEntity object matching the AddTrainingRequestDto details")
    public void should_receive_a_TrainingEntity_object_matching_the_AddTrainingRequestDto_details() {
        assertNotNull(mappedTrainingEntity);
        assertEquals(requestDto.getTrainingName(), mappedTrainingEntity.getTrainingName());
        assertEquals(requestDto.getTrainingDuration(), mappedTrainingEntity.getTrainingDuration());
        assertEquals(requestDto.getTrainingDate(), mappedTrainingEntity.getTrainingDate());
    }
}
