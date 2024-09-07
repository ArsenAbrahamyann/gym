package org.example.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
public class InMemoryStorageTest {
    private InMemoryStorage inMemoryStorage;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        inMemoryStorage = new InMemoryStorage(new ObjectMapper()) {
            @Override
            public void saveToFile() {
                try (FileWriter writer = new FileWriter(tempDir.resolve("storage.json").toFile())) {
                    Map<String, Object> dataToSave = new HashMap<>();
                    dataToSave.put("traineeStorage", getTraineeStorage());
                    dataToSave.put("trainerStorage", getTrainerStorage());
                    dataToSave.put("trainingStorage", getTrainingStorage());

                    getObjectMapper().writeValue(writer, dataToSave);

                    System.out.println("Data saved successfully to " + tempDir.resolve("storage.json"));
                } catch (IOException e) {
                    System.out.println("Error saving data to file: " + e.getMessage());
                }
            }

            @Override
            public void loadFromFile() {
                File file = tempDir.resolve("storage.json").toFile();
                if (!file.exists()) {
                    System.out.println("File does not exist. Starting fresh.");
                    return;
                }

                try {
                    Map<String, Object> loadedData = getObjectMapper().readValue(file, new TypeReference<Map<String, Object>>() {});
                    setTraineeStorage(getObjectMapper().convertValue(loadedData.get("traineeStorage"), new TypeReference<Map<String, TraineeEntity>>() {}));
                    setTrainerStorage(getObjectMapper().convertValue(loadedData.get("trainerStorage"), new TypeReference<Map<String, TrainerEntity>>() {}));
                    setTrainingStorage(getObjectMapper().convertValue(loadedData.get("trainingStorage"), new TypeReference<Map<String, TrainingEntity>>() {}));
                    System.out.println("Data loaded successfully from " + tempDir.resolve("storage.json"));
                } catch (IOException e) {
                    System.out.println("Error loading file: " + e.getMessage());
                }
            }
        };
    }

    @Test
    void testSaveToFile() throws IOException {
        TraineeEntity trainee = new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1");
        inMemoryStorage.getTraineeStorage().put("1", trainee);

        inMemoryStorage.saveToFile();

        File file = tempDir.resolve("storage.json").toFile();
        assertThat(file).exists();
        assertThat(file.length()).isGreaterThan(0);
    }

    @Test
    void testLoadFromFile() throws IOException {
        File file = tempDir.resolve("storage.json").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            Map<String, Object> dataToSave = new HashMap<>();
            Map<String, TraineeEntity> traineeStorage = new HashMap<>();
            traineeStorage.put("1", new TraineeEntity("2024-09-03T10:00:00", "123 Main St", "1"));
            dataToSave.put("traineeStorage", traineeStorage);
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(writer, dataToSave);
        }

        inMemoryStorage.loadFromFile();

        assertThat(inMemoryStorage.getTraineeStorage()).hasSize(1);
        assertThat(inMemoryStorage.getTraineeStorage().get("1")).isNotNull();
        assertThat(inMemoryStorage.getTraineeStorage().get("1").getUserId()).isEqualTo("1");
    }
}
