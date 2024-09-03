package org.example.storage;

import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage {
    private Map<String, TraineeEntity> traineeStorage = new HashMap<>();
    private Map<String, TrainerEntity> trainerStorage = new HashMap<>();
    private Map<String, TrainingEntity> trainingStorage = new HashMap<>();

    private static final String FILE_PATH = "storage.ser";


    @PostConstruct
    public void init() {
        loadFromFile();
    }

    @PreDestroy
    public void cleanup() {
        saveToFile();
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Map<String, Object> loadedData = (Map<String, Object>) ois.readObject();
            traineeStorage = (Map<String, TraineeEntity>) loadedData.get("traineeStorage");
            trainerStorage = (Map<String, TrainerEntity>) loadedData.get("trainerStorage");
            trainingStorage = (Map<String, TrainingEntity>) loadedData.get("trainingStorage");
            System.out.println("Data loaded successfully from " + FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            Map<String, Object> dataToSave = new HashMap<>();
            dataToSave.put("traineeStorage", traineeStorage);
            dataToSave.put("trainerStorage", trainerStorage);
            dataToSave.put("trainingStorage", trainingStorage);
            oos.writeObject(dataToSave);
            System.out.println("Data saved successfully to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
    public Map<String, TraineeEntity> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<String, TrainerEntity> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<String, TrainingEntity> getTrainingStorage() {
        return trainingStorage;
    }
}
