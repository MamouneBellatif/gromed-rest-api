package fr.miage.gromed.exceptions;


import org.springframework.http.HttpStatus;

public class ExpiredPanierException extends CustomException{
    public ExpiredPanierException() {
        super("expired_panier", HttpStatus.GONE);
    }
}
