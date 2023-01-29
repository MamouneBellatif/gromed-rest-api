package fr.miage.gromed.exceptions;

public class HasActivePanierException extends RuntimeException{
    public HasActivePanierException() {
        super("panier_actif_en_cours");
    }
}
