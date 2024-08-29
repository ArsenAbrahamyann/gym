package org.exemple.service;

import org.exemple.entity.Trainer;
import org.exemple.repository.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class TrainerServiceTest {
    private TrainerDAO trainerDao;
    private TrainerService trainerService;

    @BeforeEach
    public void setUp() {
        trainerDao = mock(TrainerDAO.class);
        Scanner scanner = mock(Scanner.class);
        trainerService = new TrainerService(trainerDao);
        trainerService.setScanner(scanner);
    }

    @Test
    public void shouldCreateTrainerSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("Jane", "Smith", "Fitness");
        trainerService.setScanner(scanner);

        doNothing().when(trainerDao).createTrainer(any(Trainer.class));

        trainerService.createTrainer();

        verify(trainerDao).createTrainer(any(Trainer.class));
    }

    @Test
    public void shouldUpdateTrainerSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JaneSmith", "Yoga");
        trainerService.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);
        doNothing().when(trainerDao).updateTrainer(anyString(), any(Trainer.class));

        trainerService.updateTrainer();

        verify(trainerDao).updateTrainer(anyString(), any(Trainer.class));
    }

    @Test
    public void shouldDeleteTrainerSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JaneSmith");
        trainerService.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);
        doNothing().when(trainerDao).deleteTrainer(anyString());

        trainerService.deleteTrainer();


        verify(trainerDao).deleteTrainer(anyString());
    }

    @Test
    public void shouldViewTrainerSuccessfully() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn("JaneSmith");
        trainerService.setScanner(scanner);

        Trainer existingTrainer = new Trainer();
        existingTrainer.setUserId("JaneSmith");
        existingTrainer.setSpecialization("Fitness");
        when(trainerDao.getTrainer("JaneSmith")).thenReturn(existingTrainer);

        trainerService.viewTrainer();

        verify(trainerDao).getTrainer(anyString());
    }

    @Test
    public void shouldViewAllTrainersSuccessfully() {
        List<Trainer> trainers = new ArrayList<>();
        Trainer trainer = new Trainer();
        trainer.setUserId("JaneSmith");
        trainers.add(trainer);
        when(trainerDao.getAllTrainers()).thenReturn(trainers);

        trainerService.viewAllTrainer();

        verify(trainerDao).getAllTrainers();
    }
}
