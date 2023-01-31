package fr.miage.gromed.exceptions;

public class IncompleteUtilisateurException extends RuntimeException{
    public IncompleteUtilisateurException(Long idUtilisateur) {
        super("complete_information: " + idUtilisateur);
    }
}
