package fr.miage.gromed.model.enums;

public enum TypeGenerique {
    PRINCEPS("princeps"),
    GENERIQUE("générique"),
    GENERIQUE_COMPLEMENTARITE_POSOLOGIQUE("génériques par complémentarité posologique"),
    GENERIQUE_SUBSTITUABLE("générique substituable");

    private String libelle;

    TypeGenerique(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public static TypeGenerique fromPosition(int position) {
        return TypeGenerique.values()[position];
    }


}
