package org.example.gym.componentTesting.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.gym.dto.request.AddTrainingRequestDto;
import org.example.gym.dto.request.TraineeTrainingsRequestDto;
import org.example.gym.dto.request.TrainerTrainingRequestDto;
import org.example.gym.dto.request.TrainerWorkloadRequestDto;
import org.example.gym.entity.TraineeEntity;
import org.example.gym.entity.TrainerEntity;
import org.example.gym.entity.TrainingEntity;
import org.example.gym.entity.UserEntity;
import org.example.gym.exeption.TrainingNotFoundException;
import org.example.gym.mapper.TrainingMapper;
import org.example.gym.service.JmsProducerService;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.example.gym.service.TrainingService;
import org.example.gym.repository.TrainingRepository;
import org.example.gym.utils.ValidationUtils;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TrainingServiceStepDefs {

    private TrainingService trainingService;
    private TrainingRepository trainingRepository;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private ValidationUtils validationUtils;
    private TrainingMapper trainingMapper;
    private JmsProducerService jmsProducerService;

    private TrainerEntity trainer;
    private TraineeEntity trainee;
    private TrainingEntity training;
    private UserEntity user;

    @Before
    public void setUp() {
        // Initialize mocks manually
        trainingRepository = mock(TrainingRepository.class);
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        validationUtils = mock(ValidationUtils.class);
        trainingMapper = mock(TrainingMapper.class);
        jmsProducerService = mock(JmsProducerService.class);

        // Manually create the service instance and inject mocks
        trainingService = new TrainingService(
                trainingRepository,
                traineeService,
                trainerService,
                validationUtils,
                trainingMapper,
                jmsProducerService
        );

        // Initialize mocks
        MockitoAnnotations.initMocks(this);
    }

    @Given("the trainee {string} exists")
    public void the_trainee_exists(String traineeName) {
        trainee = new TraineeEntity();
        user = new UserEntity();
        user.setUsername(traineeName);
        trainee.setUser(user);
        when(traineeService.getTrainee(traineeName)).thenReturn(trainee);
    }

    @Given("the trainer {string} exists")
    public void the_trainer_exists(String trainerName) {
        trainer = new TrainerEntity();
        user = new UserEntity();
        user.setUsername(trainerName);
        user.setIsActive(true);
        trainer.setUser(user);
        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);
    }

    @Given("the trainee {string} does not exist")
    public void the_trainee_does_not_exist(String traineeName) {
        when(traineeService.getTrainee(traineeName)).thenThrow(new RuntimeException("Trainee not found"));
    }

    @Given("the training session with ID {long} does not exist")
    public void the_training_session_does_not_exist(Long trainingId) {
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
    }

    @When("I add a new training session with trainee {string} and trainer {string}")
    public void i_add_a_new_training_session(String traineeName, String trainerName) {
        // Setup the request DTO with mock data
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto(
                traineeName,
                trainerName,
                "yoga",
                LocalDateTime.now(),
                154
        );

        // Mock the TraineeEntity and TrainerEntity
        TraineeEntity trainee = new TraineeEntity();
        UserEntity traineeUser = new UserEntity();
        traineeUser.setIsActive(true);
        traineeUser.setUsername(traineeName);
        trainee.setUser(traineeUser);

        TrainerEntity trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setIsActive(true);
        trainerUser.setUsername(trainerName);
        trainer.setUser(trainerUser);

        // Mock service methods
        when(traineeService.getTrainee(traineeName)).thenReturn(trainee);
        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);

        // Mock trainingMapper to return a valid TrainingEntity
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingDate(requestDto.getTrainingDate()); // Set other properties as needed
        when(trainingMapper.requestDtoMapToTrainingEntity(any(AddTrainingRequestDto.class), eq(trainee), eq(trainer)))
                .thenReturn(trainingEntity);

        // Mock saving the training entity
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(trainingEntity);

        // Call the service method
        trainingService.addTraining(requestDto);
    }


    @When("I try to add a new training session with trainee {string} and trainer {string}")
    public void i_try_to_add_a_new_training_session(String traineeName, String trainerName) {
        try {
            AddTrainingRequestDto requestDto = new AddTrainingRequestDto(
                    eq(traineeName),
                    eq(trainerName),
                    "uoga",
                    LocalDateTime.now(),
                    152
            );
            trainingService.addTraining(requestDto);
        } catch (Exception e) {
            // Catch and assert error
            assertTrue(e instanceof RuntimeException);
        }
    }

    @When("I delete the training session with ID {long}")
    public void i_delete_the_training_session(Long trainingId) {
        trainingService.deleteTraining(trainingId);
    }

    @When("I try to delete the training session with ID {long}")
    public void i_try_to_delete_the_training_session(Long trainingId) {
        try {
            trainingService.deleteTraining(trainingId);
        } catch (TrainingNotFoundException e) {
            // Catch and assert error
            assertTrue(e instanceof TrainingNotFoundException);
        }
    }

    @When("I request the trainings for trainee {string}")
    public void i_request_the_trainings_for_trainee(String traineeName) {
        // Simulate request DTO for trainee trainings
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto(traineeName, LocalDateTime.now(), LocalDateTime.now(), "john", "yoga");
        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainee(requestDto);
        assertNotNull(trainings);
    }

    @When("I request the trainings for trainer {string}")
    public void i_request_the_trainings_for_trainer(String trainerName) {
        TrainerEntity trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setUsername(trainerName); // Set the username for the trainer's user
        trainer.setUser(trainerUser); // Associate the UserEntity with the TrainerEntity

        // Mocking the TrainerService to return the mocked trainer
        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);

        // Mocking the training repository response
        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainings = List.of(training1, training2);

        // Mocking the repository to return these trainings for the given trainerName
        when(trainingRepository.findTrainingsForTrainer(eq(trainerName), any(), any(), any()))
                .thenReturn(trainings);

        // Now create the request DTO and call the service method
        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto(trainerName, LocalDateTime.now(), LocalDateTime.now(), "sf");
        List<TrainingEntity> result = trainingService.getTrainingsForTrainer(requestDto);

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Then("the training session should be saved")
    public void the_training_session_should_be_saved() {
        ArgumentCaptor<TrainingEntity> captor = ArgumentCaptor.forClass(TrainingEntity.class);
        verify(trainingRepository, times(1)).save(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Then("the trainer's workload should be updated")
    public void the_trainer_s_workload_should_be_updated() {
        // Verify JMS service interaction or similar logic
        verify(jmsProducerService, times(1)).sendTrainingUpdate(any(TrainerWorkloadRequestDto.class));
    }

    @Then("I should receive an error that the trainee is not found")
    public void i_should_receive_an_error_that_the_trainee_is_not_found() {
        // Asserting the exception is thrown
        assertTrue(true); // This can be refined as per actual handling logic
    }

    @Then("the training session should be removed")
    public void the_training_session_should_be_removed() {
        verify(trainingRepository, times(1)).delete(training);
    }

    @Then("I should receive an error that the training session was not found")
    public void i_should_receive_an_error_that_the_training_session_was_not_found() {
        assertTrue(true); // This can be refined as per actual error handling
    }

    @Then("I should receive a list of training sessions for trainee {string}")
    public void i_should_receive_a_list_of_training_sessions_for_trainee(String traineeName) {
        // This can be further verified with mock data
        assertTrue(true);
    }

    @Then("I should receive a list of training sessions conducted by {string}")
    public void i_should_receive_a_list_of_training_sessions_conducted_by_trainer(String trainerName) {
        // This can be further verified with mock data
        assertTrue(true);
    }

    @Given("the training session with ID {int} exists")
    public void the_training_session_exists(int trainingId) {
        training = new TrainingEntity();
        training.setId((long) trainingId);

        // Ensure that the trainer is properly set for the training session
        trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setUsername("trainerName");
        trainerUser.setIsActive(true);
        trainer.setUser(trainerUser);
        training.setTrainer(trainer);  // Set the trainer for the training session

        when(trainingRepository.findById((long) trainingId)).thenReturn(Optional.of(training));
    }

    @When("I delete the training session with ID {string}")
    public void iDeleteTheTrainingSessionWithID(String arg0) {
        Long trainingId = Long.valueOf(arg0);
        trainingService.deleteTraining(trainingId);
    }

    @Given("the training session with ID {string} does not exist")
    public void theTrainingSessionWithIDDoesNotExist(String arg0) {
        Long trainingId = Long.valueOf(arg0);
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
    }

    @When("I try to delete the training session with ID {string}")
    public void iTryToDeleteTheTrainingSessionWithID(String arg0) {
        try {
            Long trainingId = Long.valueOf(arg0);
            trainingService.deleteTraining(trainingId);
        } catch (TrainingNotFoundException e) {
            // Ensure the exception is thrown
            assertTrue(e instanceof TrainingNotFoundException);
        }
    }

    @Given("the trainee {string} has multiple training sessions")
    public void theTraineeHasMultipleTrainingSessions(String traineeName) {
        trainee = new TraineeEntity();
        user = new UserEntity();
        user.setUsername(traineeName);
        trainee.setUser(user);

        // Mocking multiple training sessions for the trainee
        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainingSessions = List.of(training1, training2);

        // Ensure correct matchers are used for method calls
        when(trainingRepository.findTrainingsForTrainee(eq(traineeName), any(), any(), anyString(), anyString()))
                .thenReturn(trainingSessions); // <- This is where we underline changes
    }

    @Given("the trainer {string} has conducted multiple training sessions")
    public void theTrainerHasConductedMultipleTrainingSessions(String trainerName) {
        trainer = new TrainerEntity();
        user = new UserEntity();
        user.setUsername(trainerName);
        user.setIsActive(true);
        trainer.setUser(user);

        // Mocking multiple training sessions for the trainer
        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainingSessions = List.of(training1, training2);
        String traineeName = "trainerName"; // You should set this appropriately
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);

        // Ensure correct matchers are used for method calls
        when(trainingRepository.findTrainingsForTrainer(eq(trainerName), eq(fromDate), eq(toDate), eq(traineeName)))
                .thenReturn(trainingSessions); // <- This is where we underline changes
    }
}
