package org.example.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class InMemoryStorage {
    private Map<String, TraineeEntity> traineeStorage = new HashMap<>();
    private Map<String, TrainerEntity> trainerStorage = new HashMap<>();
    private Map<String, TrainingEntity> trainingStorage = new HashMap<>();
    private Map<String, UserEntity> userStorage = new HashMap<>();

    private static final String FILE_PATH = "storage.json";
    private final ObjectMapper objectMapper;

    public InMemoryStorage() {
        this.objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

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

        try {
            Map<String, Object> loadedData = objectMapper.readValue(file,new TypeReference<Map<String, Object>>() {});
            traineeStorage = objectMapper.convertValue(loadedData.get("traineeStorage"),new TypeReference<Map<String, TraineeEntity>>() {});
            trainerStorage = objectMapper.convertValue(loadedData.get("trainerStorage"), new TypeReference<Map<String, TrainerEntity>>() {});
            trainingStorage = objectMapper.convertValue(loadedData.get("trainingStorage"), new TypeReference<Map<String, TrainingEntity>>() {});
            userStorage = objectMapper.convertValue(loadedData.get("userStorage"), new TypeReference<Map<String, UserEntity>>() {});
            System.out.println("Data loaded successfully from " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Map<String, Object> dataToSave = new HashMap<>();
            dataToSave.put("traineeStorage", traineeStorage);
            dataToSave.put("trainerStorage", trainerStorage);
            dataToSave.put("trainingStorage", trainingStorage);
            dataToSave.put("userStorage", userStorage);

            objectMapper.writeValue(writer, dataToSave);

            System.out.println("Data saved successfully to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }


}
