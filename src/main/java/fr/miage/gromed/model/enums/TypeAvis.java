package fr.miage.gromed.model.enums;

public enum TypeAvis {
        ASMR("ASMR"),
        SMR("SMR");

        private String type;

        TypeAvis(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

        public static TypeAvis fromString(String text) {
            for (TypeAvis b : TypeAvis.values()) {
                if (b.type.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
}
