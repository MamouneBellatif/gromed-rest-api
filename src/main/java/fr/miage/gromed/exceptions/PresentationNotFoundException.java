package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class PresentationNotFoundException extends CustomException{
    public PresentationNotFoundException() {
        super("presentation_not_found", HttpStatus.NOT_FOUND);
    }
}
