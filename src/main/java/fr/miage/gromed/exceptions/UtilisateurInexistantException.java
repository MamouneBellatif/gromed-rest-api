package fr.miage.gromed.exceptions;

public class UtilisateurInexistantException extends CustomException{
    public UtilisateurInexistantException() {
        super("utilisateur_inexistant");
    }
}
