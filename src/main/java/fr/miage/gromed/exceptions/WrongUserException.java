package fr.miage.gromed.exceptions;

public class WrongUserException extends CustomException {
    public WrongUserException() {
        super("wrong_user");
    }
}
