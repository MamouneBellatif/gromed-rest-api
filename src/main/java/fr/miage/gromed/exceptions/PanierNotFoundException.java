package fr.miage.gromed.exceptions;

public class PanierNotFoundException extends RuntimeException{
    public PanierNotFoundException() {
        super("panier_not_found");
    }
}
