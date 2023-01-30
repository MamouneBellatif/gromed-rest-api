package fr.miage.gromed.exceptions;

public class StockIndisponibleException extends RuntimeException{
    public StockIndisponibleException(long CIP, int quantite) {
        super("alerte_panier; CIP:" + CIP + "; quantite:" + quantite);
    }
}
