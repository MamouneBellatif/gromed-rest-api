package fr.miage.gromed.model.enums;

public enum ValeurAvis {       I("I"),
    II ("II"),
    III("III"),
    IV("IV"),
    V("V"),
    IMPORTANT("IMPORTANT"),
    MODERE("MODERE"),
    FAIBLE("FAIBLE"),
    INSUFFISANT("INSUFFISANT");

    private String valeur;

    ValeurAvis(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return valeur;
    }

    public static ValeurAvis fromString(String text) {
        for (ValeurAvis b : ValeurAvis.values()) {
            if (b.valeur.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
