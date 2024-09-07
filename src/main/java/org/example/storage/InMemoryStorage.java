package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Component for managing in-memory storage of entities with persistent storage capabilities.
 * <p>
 * This component maintains in-memory maps for storing entities of different types (e.g., trainees, trainers,
 * trainings, and users). It also handles the loading and saving of these entities to a JSON file to ensure data
 * persistence across application restarts.
 * </p>
 */
@Component
@PropertySource("classpath:application.properties")
@Getter
@Setter

public class InMemoryStorage {
    private Map<String, TraineeEntity> traineeStorage = new HashMap<>();
    private Map<String, TrainerEntity> trainerStorage = new HashMap<>();
    private Map<String, TrainingEntity> trainingStorage = new HashMap<>();
    private final ObjectMapper objectMapper;

    @Value(value = "${storage.file.path}")
    private String filePath;

    public InMemoryStorage(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.writerWithDefaultPrettyPrinter();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Initializes the storage by loading data from the JSON file.
     * <p>
     * This method is called after the bean's properties have been set and the bean is fully initialized.
     * It attempts to load data from the file specified by {@link #filePath}. If the file does not exist, it
     * starts with empty storage.
     * </p>
     */
    @PostConstruct
    public void init() {
        loadFromFile();
    }

    /**
     * Cleans up by saving the current state of the storage to the JSON file.
     * <p>
     * This method is called before the bean is destroyed. It ensures that any changes to the storage are
     * persisted to the file specified by {@link #filePath}.
     * </p>
     */
    @PreDestroy
    public void cleanup() {
        saveToFile();
    }

    /**
     * Loads data from the JSON file into the in-memory storage.
     * <p>
     * Reads the JSON file specified by {@link #filePath} and populates the in-memory maps with the loaded
     * data. If the file does not exist, a new storage is started.
     * </p>
     */
    public void loadFromFile() {
        File file = new File(filePath);
        if (! file.exists()) {
            System.out.println("File does not exist. Starting fresh.");
            return;
        }

        try {
            Map<String, Object> loadedData = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });
            traineeStorage = objectMapper.convertValue(loadedData.get("traineeStorage"),
                    new TypeReference<Map<String, TraineeEntity>>() {
                    });
            trainerStorage = objectMapper.convertValue(loadedData.get("trainerStorage"),
                    new TypeReference<Map<String, TrainerEntity>>() {
                    });
            trainingStorage = objectMapper.convertValue(loadedData.get("trainingStorage"),
                    new TypeReference<Map<String, TrainingEntity>>() {
                    });
            System.out.println("Data loaded successfully from "
                    + filePath);
        } catch (IOException e) {
            System.out.println("Error loading file: "
                    + e.getMessage());
        }
    }

    /**
     * Saves the current state of the in-memory storage to the JSON file.
     * <p>
     * Writes the contents of the in-memory maps to the file specified by {@link #filePath}. This method
     * ensures that the data is persisted across application restarts.
     * </p>
     */
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            Map<String, Object> dataToSave = new HashMap<>();
            dataToSave.put("traineeStorage", traineeStorage);
            dataToSave.put("trainerStorage", trainerStorage);
            dataToSave.put("trainingStorage", trainingStorage);

            objectMapper.writeValue(writer, dataToSave);

            System.out.println("Data saved successfully to "
                    + filePath);
        } catch (IOException e) {
            System.out.println("Error saving data to file: "
                    + e.getMessage());
        }
    }


}
