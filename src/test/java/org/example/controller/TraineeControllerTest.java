//package org.example.controller;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Collections;
//import java.util.List;
//import org.example.dto.TraineeDto;
//import org.example.entity.TrainerEntity;
//import org.example.service.TraineeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TraineeControllerTest {
//
//
//    @Mock
//    private TraineeService traineeService;
//
//    @InjectMocks
//    private TraineeController traineeController;
//
//    private TraineeDto mockTraineeDto;
//    private String mockUsername;
//    private String mockPassword;
//
//    /**
//     * Initializes mock objects and test data before each test case is executed.
//     * This method is annotated with {@code @BeforeEach}, ensuring that it runs
//     * before each individual test in the class. The method creates a new instance
//     * of {@link TraineeDto} and sets up mock values for the username and password
//     * fields. These objects and values will be used as test data in the test cases.
//     * This setup provides fresh and isolated instances of {@code mockTraineeDto},
//     * {@code mockUsername}, and {@code mockPassword}, ensuring that each test
//     * starts with a clean state.
//     */
//    @BeforeEach
//    void setUp() {
//        mockTraineeDto = new TraineeDto();
//        mockUsername = "johnDoe";
//        mockPassword = "newPassword";
//    }
//
////    @Test
////    void createTrainee() {
////        when(traineeService.createTraineeProfile(mockTraineeDto)).thenReturn(mockTraineeDto);
////
//////        TraineeDto result = traineeController.createTrainee(mockTraineeDto);
////
////        verify(traineeService).createTraineeProfile(mockTraineeDto);
////        assert (result.equals(mockTraineeDto));
////    }
//
//    @Test
//    void updateTraineeProfile() {
//        doNothing().when(traineeService).updateTraineeProfile(mockUsername, mockTraineeDto);
//
//        traineeController.updateTraineeProfile(mockUsername, mockTraineeDto);
//
//        verify(traineeService).updateTraineeProfile(mockUsername, mockTraineeDto);
//    }
//
//    @Test
//    void changeTraineePassword() {
//        doNothing().when(traineeService).changeTraineePassword(mockUsername, mockPassword);
//
//        traineeController.changeTraineePassword(mockUsername, mockPassword);
//
//        verify(traineeService).changeTraineePassword(mockUsername, mockPassword);
//    }
//
//    @Test
//    void toggleTraineeStatus() {
//        doNothing().when(traineeService).toggleTraineeStatus(mockUsername);
//
//        traineeController.toggleTraineeStatus(mockUsername);
//
//        verify(traineeService).toggleTraineeStatus(mockUsername);
//    }
//
//    @Test
//    void deleteTrainee() {
//        doNothing().when(traineeService).deleteTraineeByUsername(mockUsername);
//
//        traineeController.deleteTrainee(mockUsername);
//
//        verify(traineeService).deleteTraineeByUsername(mockUsername);
//    }
//
//    @Test
//    void getUnassignedTrainers() {
//        List<TrainerEntity> unassignedTrainers = Collections.emptyList();
//        when(traineeService.getUnassignedTrainers(mockUsername)).thenReturn(unassignedTrainers);
//
//        List<TrainerEntity> result = traineeController.getUnassignedTrainers(mockUsername);
//
//        verify(traineeService).getUnassignedTrainers(mockUsername);
//        assert (result.equals(unassignedTrainers));
//    }
//
//    @Test
//    void assignTrainersToTrainee() {
//        List<Long> trainerIds = List.of(1L, 2L);
//        doNothing().when(traineeService).updateTraineeTrainers(mockUsername, trainerIds);
//
//        traineeController.assignTrainersToTrainee(mockUsername, trainerIds);
//
//        verify(traineeService).updateTraineeTrainers(mockUsername, trainerIds);
//    }
//}
