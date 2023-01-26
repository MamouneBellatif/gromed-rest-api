package fr.miage.gromed.exceptions;


public class ExpiredPanierException extends RuntimeException{
    public ExpiredPanierException(String message) {
        super(message);
    }
}
