package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class PanierItemNotFoundException extends CustomException{
    public PanierItemNotFoundException() {
        super("panier_item_not_found", HttpStatus.NOT_FOUND);
    }
}
