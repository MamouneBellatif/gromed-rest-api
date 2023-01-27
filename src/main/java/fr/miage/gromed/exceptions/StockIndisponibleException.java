package fr.miage.gromed.exceptions;

public class StockIndisponibleException extends RuntimeException{
    public StockIndisponibleException() {
        super("alerte_panier");
    }
}
