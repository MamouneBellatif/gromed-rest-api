package fr.miage.gromed.model.enums;

public enum NatureComposant {
        SA("SA"),
        FT("FT"),
        ;
        private final String value;

        NatureComposant(String value) {
            this.value = value;
        }

    @Override
        public String toString() {
            return this.value;
        }
    public static NatureComposant fromString(String text) {
        for (NatureComposant b : NatureComposant.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
