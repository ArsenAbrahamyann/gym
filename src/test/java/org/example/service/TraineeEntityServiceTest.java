//package org.example.service;
//
//import org.example.entity.TraineeEntity;
//import org.example.repository.TraineeDAO;
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
//public class TraineeEntityServiceTest {
//    @Mock
//    private TraineeDAO traineeDao;
//    @Mock
//    private  Scanner scanner;
//    @InjectMocks
//    private TraineeService underTest;
//
//    @BeforeEach
//    public void setUp() {
//        underTest = new TraineeService(traineeDao);
//        underTest.setScanner(scanner);
//    }
//
//    @Test
//    public void shouldCreateTraineeSuccessfully() {
//        when(scanner.nextLine()).thenReturn("John", "Doe", "2000-01-01", "123 Main St");
//        underTest.setScanner(scanner);
//
//        doNothing().when(traineeDao).createTrainee(any(TraineeEntity.class));
//
//        underTest.createTrainee();
//
//        verify(traineeDao).createTrainee(any(TraineeEntity.class));
//    }
//
//    @Test
//    public void shouldUpdateTraineeSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JohnDoe", "2001-01-01", "456 Main St");
//        underTest.setScanner(scanner);
//
//        TraineeEntity existingTraineeEntity = new TraineeEntity();
//        existingTraineeEntity.setUserId("JohnDoe");
//        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);
//        doNothing().when(traineeDao).updateTrainee(anyString(), any(TraineeEntity.class));
//
//        underTest.updateTrainee();
//
//        verify(traineeDao).updateTrainee(anyString(), any(TraineeEntity.class));
//    }
//
//    @Test
//    public void shouldDeleteTraineeSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JohnDoe");
//        underTest.setScanner(scanner);
//
//        TraineeEntity existingTraineeEntity = new TraineeEntity();
//        existingTraineeEntity.setUserId("JohnDoe");
//        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);
//        doNothing().when(traineeDao).deleteTrainee(anyString());
//
//        underTest.deleteTrainee();
//
//        verify(traineeDao).deleteTrainee(anyString());
//    }
//
//    @Test
//    public void shouldViewTraineeSuccessfully() {
//        when(scanner.nextLine()).thenReturn("JohnDoe");
//        underTest.setScanner(scanner);
//
//        TraineeEntity existingTraineeEntity = new TraineeEntity();
//        existingTraineeEntity.setUserId("JohnDoe");
//        existingTraineeEntity.setLocalDateTime("2000-01-01");
//        existingTraineeEntity.setAddress("123 Main St");
//        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTraineeEntity);
//
//        underTest.viewTrainee();
//
//        verify(traineeDao).getTrainee(anyString());
//    }
//
//    @Test
//    public void shouldViewAllTraineesSuccessfully() {
//        List<TraineeEntity> traineeEntities = new ArrayList<>();
//        TraineeEntity traineeEntity = new TraineeEntity();
//        traineeEntity.setUserId("JohnDoe");
//        traineeEntities.add(traineeEntity);
//        when(traineeDao.getAllTrainees()).thenReturn(traineeEntities);
//
//        underTest.viewAllTrainee();
//
//        verify(traineeDao).getAllTrainees();
//    }
//}
