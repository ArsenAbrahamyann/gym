package org.exemple.storage;

import org.exemple.entity.Trainee;
import org.exemple.entity.Trainer;
import org.exemple.entity.Training;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;



import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage implements InitializingBean, DisposableBean, Serializable {
    private static final long serialVersionUID = 1111L;
    private Map<String, Trainee> traineeStorage = new HashMap<>();
    private Map<String, Trainer> trainerStorage = new HashMap<>();
    private Map<String, Training> trainingStorage = new HashMap<>();

    private final String filePath = "storage.ser";


    @Override
    public void afterPropertiesSet() throws Exception {
        loadFromFile();
    }

    @Override
    public void destroy() throws Exception {
        saveToFile();
    }

    public void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Map<String, Object> loadedData = (Map<String, Object>) ois.readObject();
            traineeStorage = (Map<String, Trainee>) loadedData.get("traineeStorage");
            trainerStorage = (Map<String, Trainer>) loadedData.get("trainerStorage");
            trainingStorage = (Map<String, Training>) loadedData.get("trainingStorage");
            System.out.println("Data loaded successfully from " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            Map<String, Object> dataToSave = new HashMap<>();
            dataToSave.put("traineeStorage", traineeStorage);
            dataToSave.put("trainerStorage", trainerStorage);
            dataToSave.put("trainingStorage", trainingStorage);
            oos.writeObject(dataToSave);
            System.out.println("Data saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    public Map<String, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<String, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<String, Training> getTrainingStorage() {
        return trainingStorage;
    }
}
