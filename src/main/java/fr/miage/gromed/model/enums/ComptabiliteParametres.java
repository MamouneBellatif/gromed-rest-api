package fr.miage.gromed.model.enums;

public enum ComptabiliteParametres {
    CHIFFRE_AFFAIRE("chiffre d'affaire"),
    CHARGES("charges"),
    NB_VENTES("nombre de ventes"),
    TRESORERIE("trésorerie")

    ;
    private final String parametreComptable;
    ComptabiliteParametres(String param) {
        this.parametreComptable=param;
    }

    @Override
    public String toString() {
        return parametreComptable;
    }
}
