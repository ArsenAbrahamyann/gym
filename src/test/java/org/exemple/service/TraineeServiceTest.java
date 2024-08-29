package org.exemple.service;

import org.exemple.entity.Trainee;
import org.exemple.repository.TraineeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TraineeServiceTest {
    private TraineeDAO traineeDao;
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        traineeDao = mock(TraineeDAO.class);
        Scanner scanner = mock(Scanner.class);
        traineeService = new TraineeService(traineeDao);
        traineeService.setScanner(scanner);
    }

    @Test
    public void shouldCreateTraineeSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("John", "Doe", "2000-01-01", "123 Main St");
        traineeService.setScanner(scanner);

        doNothing().when(traineeDao).createTrainee(any(Trainee.class));

        traineeService.createTrainee();

        verify(traineeDao).createTrainee(any(Trainee.class));
    }

    @Test
    public void shouldUpdateTraineeSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JohnDoe", "2001-01-01", "456 Main St");
        traineeService.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);
        doNothing().when(traineeDao).updateTrainee(anyString(), any(Trainee.class));

        traineeService.updateTrainee();

        verify(traineeDao).updateTrainee(anyString(), any(Trainee.class));
    }

    @Test
    public void shouldDeleteTraineeSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JohnDoe");
        traineeService.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);
        doNothing().when(traineeDao).deleteTrainee(anyString());

        traineeService.deleteTrainee();

        verify(traineeDao).deleteTrainee(anyString());
    }

    @Test
    public void shouldViewTraineeSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JohnDoe");
        traineeService.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        existingTrainee.setLocalDateTime("2000-01-01");
        existingTrainee.setAddress("123 Main St");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);

        traineeService.viewTrainee();

        verify(traineeDao).getTrainee(anyString());
    }

    @Test
    public void shouldViewAllTraineesSuccessfully() {
        List<Trainee> trainees = new ArrayList<>();
        Trainee trainee = new Trainee();
        trainee.setUserId("JohnDoe");
        trainees.add(trainee);
        when(traineeDao.getAllTrainees()).thenReturn(trainees);

        traineeService.viewAllTrainee();

        verify(traineeDao).getAllTrainees();
    }
}
