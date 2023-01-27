package fr.miage.gromed.model.enums;

public enum TypeAlerte {
    PANIER("panier"),
    PANIER_ITEM("panier_item");

    private final String type;

    TypeAlerte(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
