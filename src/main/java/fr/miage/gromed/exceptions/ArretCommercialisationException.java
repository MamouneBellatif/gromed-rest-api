package fr.miage.gromed.exceptions;

public class ArretCommercialisationException extends CustomException {
    public ArretCommercialisationException() {
        super("Le médicament n'est plus commercialisé");
    }

}
