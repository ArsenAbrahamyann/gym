package org.example.service;

import org.example.entity.Trainer;
import org.example.repository.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TrainerServiceTest {
    private final TrainerDAO trainerDao = mock(TrainerDAO.class);
    private final Scanner scanner = mock(Scanner.class);
    private TrainerService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new TrainerService(trainerDao);
        underTest.setScanner(scanner);
    }

    @Test
    public void shouldCreateTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("Jane", "Smith", "Fitness");
        underTest.setScanner(scanner);

        doNothing().when(trainerDao).createTrainer(any(Trainer.class));

        underTest.createTrainer();

        verify(trainerDao).createTrainer(any(Trainer.class));
    }

    @Test
    public void shouldUpdateTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith", "Yoga");
        underTest.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);
        doNothing().when(trainerDao).updateTrainer(anyString(), any(Trainer.class));

        underTest.updateTrainer();

        verify(trainerDao).updateTrainer(anyString(), any(Trainer.class));
    }

    @Test
    public void shouldDeleteTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith");
        underTest.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);
        doNothing().when(trainerDao).deleteTrainer(anyString());

        underTest.deleteTrainer();


        verify(trainerDao).deleteTrainer(anyString());
    }

    @Test
    public void shouldViewTrainerSuccessfully() {
        when(scanner.nextLine()).thenReturn("JaneSmith");
        underTest.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        existingTrainer.setSpecialization("Fitness");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);

        underTest.viewTrainer();

        verify(trainerDao).getTrainer(anyString());
    }

    @Test
    public void shouldViewAllTrainersSuccessfully() {
        List<Trainer> trainers = new ArrayList<>();
        Trainer trainer = new Trainer();
        trainer.setUserId("JaneSmith");
        trainers.add(trainer);
        when(trainerDao.getAllTrainers()).thenReturn(trainers);

        underTest.viewAllTrainer();

        verify(trainerDao).getAllTrainers();
    }
}
