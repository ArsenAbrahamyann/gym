//package org.example.storage;
//
//import org.example.entity.TraineeEntity;
//import org.example.entity.TrainerEntity;
//import org.example.entity.TrainingEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Fail.fail;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class InMemoryStorageTest {
//    @InjectMocks
//    private InMemoryStorage inMemoryStorage;
//
//    @BeforeEach
//    void setUp() {
//        inMemoryStorage = new InMemoryStorage();
//    }
//
//    @Test
//    void testLoadFromFileWhenFileExists() throws Exception {
//        Map<String, Object> mockData = new HashMap<>();
//        mockData.put("traineeStorage", new HashMap<String, TraineeEntity>());
//        mockData.put("trainerStorage", new HashMap<String, TrainerEntity>());
//        mockData.put("trainingStorage", new HashMap<String, TrainingEntity>());
//
//        File tempFile = File.createTempFile("storage", ".ser");
//        setPrivateField(inMemoryStorage, "FILE_PATH", tempFile.getAbsolutePath());
//
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
//            oos.writeObject(mockData);
//        }
//
//        inMemoryStorage.loadFromFile();
//
//    }
//
//    @Test
//    void testSaveToFile() throws Exception {
//        File tempFile = File.createTempFile("storage", ".ser");
//        setPrivateField(inMemoryStorage, "FILE_PATH", tempFile.getAbsolutePath());
//
//        inMemoryStorage.saveToFile();
//
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tempFile))) {
//            Map<String, Object> data = (Map<String, Object>) ois.readObject();
//            assertNotNull(data);
//        } catch (ClassNotFoundException e) {
//            fail("Failed to read the file: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testLoadFromFileWhenFileDoesNotExist() throws Exception {
//        File file = new File(getPrivateField(inMemoryStorage, "FILE_PATH").toString());
//        if (file.exists()) {
//            file.delete();
//        }
//
//        inMemoryStorage.loadFromFile();
//
//    }
//
//    @Test
//    void testSaveToFileHandlesException() throws Exception {
//        File tempFile = File.createTempFile("storage", ".ser");
//        setPrivateField(inMemoryStorage, "FILE_PATH", tempFile.getAbsolutePath());
//
//        doThrow(new IOException("Mocked IOException")).when(inMemoryStorage).saveToFile();
//
//        inMemoryStorage.saveToFile();
//
//    }
//
//    private void setPrivateField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
//        Field field = obj.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        field.set(obj, value);
//    }
//
//    private Object getPrivateField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Field field = obj.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        return field.get(obj);
//    }
//}
