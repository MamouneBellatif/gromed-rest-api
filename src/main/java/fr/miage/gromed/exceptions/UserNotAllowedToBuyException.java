package fr.miage.gromed.exceptions;

public class UserNotAllowedToBuyException extends RuntimeException {

    public UserNotAllowedToBuyException() {
        super("achat_non_autoirsé");
    }
}
