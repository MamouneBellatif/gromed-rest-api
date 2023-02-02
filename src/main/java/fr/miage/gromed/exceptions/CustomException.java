package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    HttpStatus status;
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
