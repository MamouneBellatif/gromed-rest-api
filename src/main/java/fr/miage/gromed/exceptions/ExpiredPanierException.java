package fr.miage.gromed.exceptions;


public class ExpiredPanierException extends RuntimeException{
    public ExpiredPanierException() {
        super("expired_panier");
    }
}
