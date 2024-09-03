package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.repository.TrainingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for managing trainings.
 */
@Service
@Slf4j
public class TrainingService {
    private final TrainingDAO trainingDao;

    @Autowired
    public TrainingService(TrainingDAO trainingDao) {
        this.trainingDao = trainingDao;
    }

    public void createTraining(TrainingEntity trainingEntity) {
        trainingDao.createTraining(trainingEntity);
    }

    public void updateTraining(String trainingName, TrainingEntity existingTrainingEntity) {
        trainingDao.updateTraining(trainingName, existingTrainingEntity);
    }

    public void deleteTraining(String trainingName) {
        trainingDao.deleteTraining(trainingName);
    }


    public TrainingEntity getTraining(String trainingName) {
        return trainingDao.getTraining(trainingName);
    }

    public List<TrainingEntity> getAllTrainings() {
        return trainingDao.getAllTrainings();
    }


}
