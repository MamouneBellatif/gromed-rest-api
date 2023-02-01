package fr.miage.gromed.exceptions;

public class HasActivePanierException extends CustomException{
    public HasActivePanierException() {
        super("panier_actif_en_cours");
    }
}
