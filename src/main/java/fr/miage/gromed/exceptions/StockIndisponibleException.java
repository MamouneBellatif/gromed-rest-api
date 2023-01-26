package fr.miage.gromed.exceptions;

public class StockIndisponibleException extends RuntimeException{
    public StockIndisponibleException(String message) {
        super(message);
    }
}
