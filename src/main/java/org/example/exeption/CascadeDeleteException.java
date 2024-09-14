package org.example.exeption;

public class CascadeDeleteException extends RuntimeException {
    public CascadeDeleteException(String message) {
        super(message);
    }
}
