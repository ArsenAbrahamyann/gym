//package org.example.service;
//
//import org.example.entity.TrainingEntity;
//import org.example.entity.TrainingTypeEntity;
//import org.example.repository.TrainingDAO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingEntityServiceTest {
//    @Mock
//    private TrainingDAO trainingDao;
//    @Mock
//    private Scanner scanner;
//    @InjectMocks
//    private TrainingService underTest;
//
//    @BeforeEach
//    public void setUp() {
//        underTest = new TrainingService(trainingDao);
//        underTest.setScanner(scanner);
//    }
//
//    @Test
//    public void shouldCreateTrainingSuccessfully() {
//        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "01:30");
//        underTest.setScanner(scanner);
//
//        doNothing().when(trainingDao).createTraining(any(TrainingEntity.class));
//
//        underTest.createTraining();
//
//        verify(trainingDao).createTraining(any(TrainingEntity.class));
//    }
//
//    @Test
//    public void shouldUpdateTrainingSuccessfully() {
//        when(scanner.nextLine()).thenReturn("Training1", "trainee2", "trainer2", "UpdatedTraining", "Pilates", "2024-10-01", "02:00");
//        underTest.setScanner(scanner);
//
//        TrainingEntity existingTrainingEntity = new TrainingEntity();
//        existingTrainingEntity.setTrainingName("Training1");
//        when(trainingDao.getTraining("Training1")).thenReturn(existingTrainingEntity);
//        doNothing().when(trainingDao).updateTraining(anyString(), any(TrainingEntity.class));
//
//        underTest.updateTraining();
//
//        verify(trainingDao).updateTraining(anyString(), any(TrainingEntity.class));
//    }
//
//    @Test
//    public void shouldDeleteTrainingSuccessfully() {
//        when(scanner.nextLine()).thenReturn("Training1");
//        underTest.setScanner(scanner);
//
//        TrainingEntity existingTrainingEntity = new TrainingEntity();
//        existingTrainingEntity.setTrainingName("Training1");
//        when(trainingDao.getTraining("Training1")).thenReturn(existingTrainingEntity);
//        doNothing().when(trainingDao).deleteTraining(anyString());
//
//        underTest.deleteTraining();
//
//        verify(trainingDao).deleteTraining(anyString());
//    }
//
//    @Test
//    public void shouldViewTrainingSuccessfully() {
//        when(scanner.nextLine()).thenReturn("Training1");
//        underTest.setScanner(scanner);
//
//        TrainingEntity existingTrainingEntity = new TrainingEntity();
//        existingTrainingEntity.setTrainingName("Training1");
//        existingTrainingEntity.setTraineeId("trainee1");
//        existingTrainingEntity.setTrainerId("trainer1");
//        existingTrainingEntity.setTrainingTypeEntity(new TrainingTypeEntity("Yoga"));
//        existingTrainingEntity.setTrainingDate("2024-09-01");
//        existingTrainingEntity.setTrainingDuration(Duration.ofHours(1).plusMinutes(30));
//        when(trainingDao.getTraining("Training1")).thenReturn(existingTrainingEntity);
//
//        underTest.viewTraining();
//
//        verify(trainingDao).getTraining(anyString());
//    }
//
//    @Test
//    public void shouldViewAllTrainingsSuccessfully() {
//        when(scanner.nextLine()).thenReturn("trainee1", "trainer1", "Training1", "Yoga", "2024-09-01", "invalid-duration");
//        underTest.setScanner(scanner);
//
//        underTest.createTraining();
//
//        verify(trainingDao, never()).createTraining(any(TrainingEntity.class));
//    }
//
//
//}
