package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class PanierCantBeCanceledException extends CustomException {

        public PanierCantBeCanceledException() {
            super("delais_annulation_depasse", HttpStatus.NOT_ACCEPTABLE);
        }
}
