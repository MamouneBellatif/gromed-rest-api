package fr.miage.gromed.exceptions;

public class ArretCommercialisationException extends RuntimeException {
    public ArretCommercialisationException() {
        super("Le médicament n'est plus commercialisé");
    }

}
