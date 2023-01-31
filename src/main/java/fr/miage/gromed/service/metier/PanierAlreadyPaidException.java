package fr.miage.gromed.service.metier;

public class PanierAlreadyPaidException extends RuntimeException {

        public PanierAlreadyPaidException() {
            super("panier_deja_paye");
        }
}
