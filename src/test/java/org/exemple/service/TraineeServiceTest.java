package org.exemple.service;

import org.exemple.entity.Trainee;
import org.exemple.repository.TraineeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TraineeServiceTest {
    private final TraineeDAO traineeDao = mock(TraineeDAO.class);
    private final Scanner scanner = mock(Scanner.class);
    private TraineeService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TraineeService(traineeDao);
        underTest.setScanner(scanner);
    }

    @Test
    public void shouldCreateTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("John", "Doe", "2000-01-01", "123 Main St");
        underTest.setScanner(scanner);

        doNothing().when(traineeDao).createTrainee(any(Trainee.class));

        underTest.createTrainee();

        verify(traineeDao).createTrainee(any(Trainee.class));
    }

    @Test
    public void shouldUpdateTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe", "2001-01-01", "456 Main St");
        underTest.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);
        doNothing().when(traineeDao).updateTrainee(anyString(), any(Trainee.class));

        underTest.updateTrainee();

        verify(traineeDao).updateTrainee(anyString(), any(Trainee.class));
    }

    @Test
    public void shouldDeleteTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe");
        underTest.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);
        doNothing().when(traineeDao).deleteTrainee(anyString());

        underTest.deleteTrainee();

        verify(traineeDao).deleteTrainee(anyString());
    }

    @Test
    public void shouldViewTraineeSuccessfully() {
        when(scanner.nextLine()).thenReturn("JohnDoe");
        underTest.setScanner(scanner);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUserId("JohnDoe");
        existingTrainee.setLocalDateTime("2000-01-01");
        existingTrainee.setAddress("123 Main St");
        when(traineeDao.getTrainee("JohnDoe")).thenReturn(existingTrainee);

        underTest.viewTrainee();

        verify(traineeDao).getTrainee(anyString());
    }

    @Test
    public void shouldViewAllTraineesSuccessfully() {
        List<Trainee> trainees = new ArrayList<>();
        Trainee trainee = new Trainee();
        trainee.setUserId("JohnDoe");
        trainees.add(trainee);
        when(traineeDao.getAllTrainees()).thenReturn(trainees);

        underTest.viewAllTrainee();

        verify(traineeDao).getAllTrainees();
    }
}
