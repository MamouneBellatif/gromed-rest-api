package fr.miage.gromed.service.metier;

import fr.miage.gromed.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class PanierAlreadyPaidException extends CustomException {

        public PanierAlreadyPaidException() {
            super("panier_deja_paye", HttpStatus.NOT_ACCEPTABLE);
        }
}
