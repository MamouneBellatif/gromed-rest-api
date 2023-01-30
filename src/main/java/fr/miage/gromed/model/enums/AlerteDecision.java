package fr.miage.gromed.model.enums;

public enum AlerteDecision {
    DELAI_LIVRAISON("delai de livraison"),
    ANNULER_ITEM("annuler l'item"),
    ANNULER_PANIER("annuler le panier")

    ;
    private String decision;

    AlerteDecision(String decision) {
        this.decision = decision;
    }

    @Override
    public String toString() {
        return decision;
    }
}
