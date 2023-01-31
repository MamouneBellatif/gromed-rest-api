package fr.miage.gromed.exceptions;


public class AucunPanierActifException extends RuntimeException {
    public AucunPanierActifException() {
        super("aucun_panier_actif");
    }
}
