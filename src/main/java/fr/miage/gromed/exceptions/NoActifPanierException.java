package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class NoActifPanierException extends CustomException{
    public NoActifPanierException() {
        super("Pas de panier actif", HttpStatus.PARTIAL_CONTENT);
    }
}
