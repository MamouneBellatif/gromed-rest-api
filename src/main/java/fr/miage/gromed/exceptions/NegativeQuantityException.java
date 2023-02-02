package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class NegativeQuantityException extends CustomException {
    public NegativeQuantityException() {
        super("Quantité négative", HttpStatus.BAD_REQUEST);
    }
}
