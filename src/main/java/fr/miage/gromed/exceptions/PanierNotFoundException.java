package fr.miage.gromed.exceptions;

public class PanierNotFoundException extends CustomException{
    public PanierNotFoundException() {
        super("panier_not_found");
    }
}
