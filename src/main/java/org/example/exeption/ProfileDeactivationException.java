package org.example.exeption;

public class ProfileDeactivationException extends RuntimeException {
    public ProfileDeactivationException(String message) {
        super(message);
    }
}
