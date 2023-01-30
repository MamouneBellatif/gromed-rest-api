package fr.miage.gromed.exceptions;

public class PresentationNotFoundException extends RuntimeException{
    public PresentationNotFoundException() {
        super("presentation_not_found");
    }
}
