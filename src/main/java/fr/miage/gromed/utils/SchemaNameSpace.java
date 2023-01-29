package fr.miage.gromed.utils;

public enum SchemaNameSpace {
    MEDICAMENT("médicaments"),
    PRESENTATION("présentation"),
    COMPOSANT("composant"),

    INFOS("infos"),

    GROUPE_GENERIQUE("groupe_générique"),

    AVIS_ASMR("avis_asmr"),

    AVIS_SMR("avis_smr"),
    CONDITION_PRESCRIPTION("conditions"),
    URL("url");







    private String tableName;
    SchemaNameSpace(String tableName) {
        this.tableName=tableName;
    }

    @Override
    public String toString(){
        return tableName;
    }
}