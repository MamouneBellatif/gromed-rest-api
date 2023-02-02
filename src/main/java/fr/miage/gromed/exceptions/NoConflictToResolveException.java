package fr.miage.gromed.exceptions;

public class NoConflictToResolveException extends CustomException {
    public NoConflictToResolveException() {
        super("pas_de_conflit");
    }
}
