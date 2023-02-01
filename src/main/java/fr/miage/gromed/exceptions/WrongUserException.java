package fr.miage.gromed.exceptions;

import org.springframework.http.HttpStatus;

public class WrongUserException extends CustomException {


    public WrongUserException() {
        super("wrong_user", HttpStatus.FORBIDDEN);
    }
}
