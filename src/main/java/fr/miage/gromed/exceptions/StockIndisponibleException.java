package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class StockIndisponibleException extends CustomException{
    public StockIndisponibleException(long CIP, int quantite) {
        super("alerte_panier_attente_de_reponse; CIP:" + CIP + "; quantite:" + quantite, HttpStatus.MULTIPLE_CHOICES);
    }
}
