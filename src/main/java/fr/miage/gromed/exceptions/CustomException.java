package fr.miage.gromed.exceptions;

public abstract class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

}
