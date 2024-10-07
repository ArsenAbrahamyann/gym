package org.example.gym.exeption;

public class TraineeNotFoundException extends RuntimeException{
    public TraineeNotFoundException(String message) {
        super(message);
    }
}
