package fr.miage.gromed.exceptions;

public class PanierCantBeCanceledException extends RuntimeException {

        public PanierCantBeCanceledException() {
            super("delais_annulation_depasse");
        }
}
