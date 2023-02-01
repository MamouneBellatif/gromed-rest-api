package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class PanierNotFoundException extends CustomException{
    public PanierNotFoundException() {
        super("panier_not_found", HttpStatus.NOT_FOUND);
    }
}
