package fr.miage.gromed.exceptions;

public class UserNotAllowedToBuyException extends CustomException {

    public UserNotAllowedToBuyException() {
        super("achat_non_autoirsé");
    }
}
