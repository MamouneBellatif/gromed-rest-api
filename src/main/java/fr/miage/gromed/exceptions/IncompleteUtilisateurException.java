package fr.miage.gromed.exceptions;

public class IncompleteUtilisateurException extends CustomException{
    public IncompleteUtilisateurException(Long idUtilisateur) {
        super("complete_information: " + idUtilisateur);
    }
}
