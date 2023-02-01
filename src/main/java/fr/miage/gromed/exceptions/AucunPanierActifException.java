package fr.miage.gromed.exceptions;


import org.springframework.http.HttpStatus;

public class AucunPanierActifException extends CustomException {
    public AucunPanierActifException() {
        super("aucun_panier_actif", HttpStatus.BAD_REQUEST);
    }
}
