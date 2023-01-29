package fr.miage.gromed.model.enums;

import java.util.stream.Stream;

public enum ComptabiliteParametres {
    CHIFFRE_AFFAIRE("chiffre_affaire"),
    CHARGES("charges"),
    NB_VENTES("nombre_de_ventes"),
    TRESORERIE("tresorerie")

    ;
    private final String parametreComptable;
    ComptabiliteParametres(String param) {
        this.parametreComptable=param;
    }

    @Override
    public String toString() {
        return parametreComptable;
    }

    public static Stream<ComptabiliteParametres> getComptabiliteParameters(){
        return Stream.of(ComptabiliteParametres.values());
    }

}
