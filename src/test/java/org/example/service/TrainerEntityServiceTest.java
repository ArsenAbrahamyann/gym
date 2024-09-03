//package org.example.service;
//
//import org.example.entity.TrainerEntity;
//import org.example.repository.TrainerDAO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainerEntityServiceTest {
//    @Mock
//    private TrainerDAO trainerDao;
//    @Mock
//    private Scanner scanner;
//    @InjectMocks
//    private TrainerService underTest;
//
//    @BeforeEach
//    public void setUp() {
//        underTest = new TrainerService(trainerDao);
//        underTest.setScanner(scanner);
//    }
//
//    @Test
//    public void shouldCreateTrainerSuccessfully() {
//        when(scanner.nextLine()).thenReturn("Jane", "Smith", "Fitness");
//        underTest.setScanner(scanner);
//
//        doNothing().when(trainerDao).createTrainer(any(TrainerEntity.class));
//
//        underTest.createTrainer();
//
//        verify(trainerDao).createTrainer(any(TrainerEntity.class));
//    }
//
//    @Test
//    public void shouldUpdateTrainerSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JaneSmith", "Yoga");
//        underTest.setScanner(scanner);
//
//        TrainerEntity existingTrainerEntity = new TrainerEntity();
//        existingTrainerEntity.setUserId("JaneSmith");
//        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
//        doNothing().when(trainerDao).updateTrainer(anyString(), any(TrainerEntity.class));
//
//        underTest.updateTrainer();
//
//        verify(trainerDao).updateTrainer(anyString(), any(TrainerEntity.class));
//    }
//
//    @Test
//    public void shouldDeleteTrainerSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JaneSmith");
//        underTest.setScanner(scanner);
//
//        TrainerEntity existingTrainerEntity = new TrainerEntity();
//        existingTrainerEntity.setUserId("JaneSmith");
//        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
//        doNothing().when(trainerDao).deleteTrainer(anyString());
//
//        underTest.deleteTrainer();
//
//
//        verify(trainerDao).deleteTrainer(anyString());
//    }
//
//    @Test
//    public void shouldViewTrainerSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JaneSmith");
//        underTest.setScanner(scanner);
//
//        TrainerEntity existingTrainerEntity = new TrainerEntity();
//        existingTrainerEntity.setUserId("JaneSmith");
//        existingTrainerEntity.setSpecialization("Fitness");
//        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainerEntity);
//
//        underTest.viewTrainer();
//
//        verify(trainerDao).getTrainer(anyString());
//    }
//
//    @Test
//    public void shouldViewAllTrainersSuccessfully() {
//        List<TrainerEntity> trainerEntities = new ArrayList<>();
//        TrainerEntity trainerEntity = new TrainerEntity();
//        trainerEntity.setUserId("JaneSmith");
//        trainerEntities.add(trainerEntity);
//        when(trainerDao.getAllTrainers()).thenReturn(trainerEntities);
//
//        underTest.viewAllTrainer();
//
//        verify(trainerDao).getAllTrainers();
//    }
//}
