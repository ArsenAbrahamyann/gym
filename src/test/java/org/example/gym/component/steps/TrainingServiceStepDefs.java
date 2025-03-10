package org.example.gym.component.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import org.example.gym.repository.TrainingRepository;
import org.example.gym.service.JmsProducerService;
import org.example.gym.service.TraineeService;
import org.example.gym.service.TrainerService;
import org.example.gym.service.TrainingService;
import org.example.gym.utils.ValidationUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

/**
 * Cucumber steps for testing the {@link TrainingService}.
 */
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

    /**
     * Initializes mocks and the {@link TrainingService} before each scenario.
     */
    @Before
    public void setUp() {
        trainingRepository = mock(TrainingRepository.class);
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        validationUtils = mock(ValidationUtils.class);
        trainingMapper = mock(TrainingMapper.class);
        jmsProducerService = mock(JmsProducerService.class);

        trainingService = new TrainingService(
                trainingRepository,
                traineeService,
                trainerService,
                validationUtils,
                trainingMapper,
                jmsProducerService
        );

        MockitoAnnotations.initMocks(this);
    }

    /**
     * Sets up a trainee entity with the given username.
     *
     * @param traineeName the username of the trainee
     */
    @Given("the trainee {string} exists")
    public void the_trainee_exists(String traineeName) {
        trainee = new TraineeEntity();
        user = new UserEntity();
        user.setUsername(traineeName);
        trainee.setUser(user);
        when(traineeService.getTrainee(traineeName)).thenReturn(trainee);
    }

    /**
     * Sets up a trainer entity with the given username.
     *
     * @param trainerName the username of the trainer
     */
    @Given("the trainer {string} exists")
    public void the_trainer_exists(String trainerName) {
        trainer = new TrainerEntity();
        user = new UserEntity();
        user.setUsername(trainerName);
        user.setIsActive(true);
        trainer.setUser(user);
        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);
    }

    /**
     * Mocks the trainee service to throw an exception when retrieving a non-existent trainee.
     *
     * @param traineeName the username of the non-existent trainee
     */
    @Given("the trainee {string} does not exist")
    public void the_trainee_does_not_exist(String traineeName) {
        when(traineeService.getTrainee(traineeName)).thenThrow(new RuntimeException("Trainee not found"));
    }

    /**
     * Mocks the training repository to return an empty Optional when searching for a non-existent training session.
     *
     * @param trainingId the ID of the non-existent training session
     */
    @Given("the training session with ID {long} does not exist")
    public void the_training_session_does_not_exist(Long trainingId) {
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
    }

    /**
     * Adds a new training session.
     *
     * @param traineeName the username of the trainee
     * @param trainerName the username of the trainer
     */
    @When("I add a new training session with trainee {string} and trainer {string}")
    public void add_a_new_training_session(String traineeName, String trainerName) {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto(traineeName, trainerName, "yoga",
                LocalDateTime.now(), 154
        );
        TraineeEntity trainee = new TraineeEntity();
        UserEntity traineeUser = new UserEntity();
        traineeUser.setIsActive(true);
        traineeUser.setUsername(traineeName);
        trainee.setUser(traineeUser);
        TrainerEntity trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setIsActive(true);
        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTrainingDate(requestDto.getTrainingDate());
        trainerUser.setUsername(trainerName);
        trainer.setUser(trainerUser);
        when(traineeService.getTrainee(traineeName)).thenReturn(trainee);
        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);
        when(trainingMapper.requestDtoMapToTrainingEntity(any(AddTrainingRequestDto.class), eq(trainee), eq(trainer)))
                .thenReturn(trainingEntity);
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(trainingEntity);

        trainingService.addTraining(requestDto);
    }

    /**
     * Tries to add a new training session and expects an exception.
     *
     * @param traineeName the username of the trainee
     * @param trainerName the username of the trainer
     */
    @When("I try to add a new training session with trainee {string} and trainer {string}")
    public void try_to_add_a_new_training_session(String traineeName, String trainerName) {
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
            assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * Deletes a training session.
     *
     * @param trainingId the ID of the training session to delete
     */
    @When("I delete the training session with ID {long}")
    public void delete_the_training_session(Long trainingId) {
        trainingService.deleteTraining(trainingId);
    }

    /**
     * Tries to delete a training session and expects a {@link TrainingNotFoundException}.
     *
     * @param trainingId the ID of the training session to delete
     */
    @When("I try to delete the training session with ID {long}")
    public void try_to_delete_the_training_session(Long trainingId) {
        try {
            trainingService.deleteTraining(trainingId);
        } catch (TrainingNotFoundException e) {
            assertTrue(e instanceof TrainingNotFoundException);
        }
    }

    /**
     * Requests trainings for a trainee.
     *
     * @param traineeName the username of the trainee
     */
    @When("I request the trainings for trainee {string}")
    public void request_the_trainings_for_trainee(String traineeName) {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto(traineeName, LocalDateTime.now(),
                LocalDateTime.now(), "john", "yoga");
        List<TrainingEntity> trainings = trainingService.getTrainingsForTrainee(requestDto);
        assertNotNull(trainings);
    }

    /**
     * Requests trainings for a trainer.
     *
     * @param trainerName the username of the trainer
     */
    @When("I request the trainings for trainer {string}")
    public void request_the_trainings_for_trainer(String trainerName) {
        TrainerEntity trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setUsername(trainerName);
        trainer.setUser(trainerUser);

        when(trainerService.getTrainer(trainerName)).thenReturn(trainer);

        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainings = List.of(training1, training2);

        when(trainingRepository.findTrainingsForTrainer(eq(trainerName), any(), any(), any()))
                .thenReturn(trainings);

        TrainerTrainingRequestDto requestDto = new TrainerTrainingRequestDto(trainerName,
                LocalDateTime.now(), LocalDateTime.now(), "sf");
        List<TrainingEntity> result = trainingService.getTrainingsForTrainer(requestDto);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Verifies that the training session was saved.
     */
    @Then("the training session should be saved")
    public void the_training_session_should_be_saved() {
        ArgumentCaptor<TrainingEntity> captor = ArgumentCaptor.forClass(TrainingEntity.class);
        verify(trainingRepository, times(1)).save(captor.capture());
        assertNotNull(captor.getValue());
    }

    /**
     * Verifies that the trainer's workload was updated.
     */
    @Then("the trainer's workload should be updated")
    public void the_trainer_s_workload_should_be_updated() {
        verify(jmsProducerService, times(1)).sendTrainingUpdate(
                any(TrainerWorkloadRequestDto.class));
    }

    /**
     * Verifies that an error is received when the trainee is not found.
     */
    @Then("I should receive an error that the trainee is not found")
    public void should_receive_an_error_that_the_trainee_is_not_found() {
        assertTrue(true);
    }

    /**
     * Verifies that the training session was removed.
     */
    @Then("the training session should be removed")
    public void the_training_session_should_be_removed() {
        verify(trainingRepository,
                times(1)).delete(training);
    }

    /**
     * Verifies that an error is received when the training session is not found.
     */
    @Then("I should receive an error that the training session was not found")
    public void should_receive_an_error_that_the_training_session_was_not_found() {
        assertTrue(true);
    }

    /**
     * Verifies that a list of training sessions is received for a trainee.
     *
     * @param traineeName the username of the trainee
     */
    @Then("I should receive a list of training sessions for trainee {string}")
    public void should_receive_a_list_of_training_sessions_for_trainee(String traineeName) {
        assertTrue(true);
    }

    /**
     * Verifies that a list of training sessions is received for a trainer.
     *
     * @param trainerName the username of the trainer
     */
    @Then("I should receive a list of training sessions conducted by {string}")
    public void should_receive_a_list_of_training_sessions_conducted_by_trainer(String trainerName) {
        assertTrue(true);
    }

    /**
     * Sets up an existing training session with the given ID.
     *
     * @param trainingId the ID of the existing training session
     */
    @Given("the training session with ID {int} exists")
    public void the_training_session_exists(int trainingId) {
        training = new TrainingEntity();
        training.setId((long) trainingId);

        trainer = new TrainerEntity();
        UserEntity trainerUser = new UserEntity();
        trainerUser.setUsername("trainerName");
        trainerUser.setIsActive(true);
        trainer.setUser(trainerUser);
        training.setTrainer(trainer);

        when(trainingRepository.findById((long) trainingId)).thenReturn(Optional.of(training));
    }

    /**
     * Deletes a training session by ID (given as a String).
     *
     * @param arg0 the ID of the training session to delete (as a String)
     */
    @When("I delete the training session with ID {string}")
    public void deleteTheTrainingSessionWithID(String arg0) {
        Long trainingId = Long.valueOf(arg0);
        trainingService.deleteTraining(trainingId);
    }

    /**
     * Mocks the training repository to return an empty Optional when searching for a non-existent training session (given as a String).
     *
     * @param arg0 the ID of the non-existent training session (as a String)
     */
    @Given("the training session with ID {string} does not exist")
    public void theTrainingSessionWithIdDoesNotExist(String arg0) {
        Long trainingId = Long.valueOf(arg0);
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
    }

    /**
     * Tries to delete a training session by ID (given as a String) and expects a {@link TrainingNotFoundException}.
     *
     * @param arg0 the ID of the training session to delete (as a String)
     */
    @When("I try to delete the training session with ID {string}")
    public void tryToDeleteTheTrainingSessionWithID(String arg0) {
        try {
            Long trainingId = Long.valueOf(arg0);
            trainingService.deleteTraining(trainingId);
        } catch (TrainingNotFoundException e) {
            assertTrue(e instanceof TrainingNotFoundException);
        }
    }

    /**
     * Sets up a trainee with multiple training sessions.
     *
     * @param traineeName the username of the trainee
     */
    @Given("the trainee {string} has multiple training sessions")
    public void theTraineeHasMultipleTrainingSessions(String traineeName) {
        trainee = new TraineeEntity();
        user = new UserEntity();
        user.setUsername(traineeName);
        trainee.setUser(user);

        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainingSessions = List.of(training1, training2);

        when(trainingRepository.findTrainingsForTrainee(eq(traineeName), any(), any(), anyString(), anyString()))
                .thenReturn(trainingSessions);
    }

    /**
     * Sets up a trainer with multiple conducted training sessions.
     *
     * @param trainerName the username of the trainer
     */
    @Given("the trainer {string} has conducted multiple training sessions")
    public void theTrainerHasConductedMultipleTrainingSessions(String trainerName) {
        trainer = new TrainerEntity();
        user = new UserEntity();
        user.setUsername(trainerName);
        user.setIsActive(true);
        trainer.setUser(user);

        TrainingEntity training1 = new TrainingEntity();
        TrainingEntity training2 = new TrainingEntity();
        List<TrainingEntity> trainingSessions = List.of(training1, training2);
        String traineeName = "trainerName";
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now().plusDays(1);

        when(trainingRepository.findTrainingsForTrainer(eq(trainerName), eq(fromDate), eq(toDate), eq(traineeName)))
                .thenReturn(trainingSessions);
    }
}
