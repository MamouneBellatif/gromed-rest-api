package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class PanierCanceledException extends CustomException {

        public PanierCanceledException() {
            super("panier_annule", HttpStatus.GONE);
        }
}
