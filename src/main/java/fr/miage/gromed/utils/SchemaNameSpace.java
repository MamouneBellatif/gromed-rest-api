package fr.miage.gromed.utils;

public enum SchemaNameSpace {
    MEDICAMENT("médicaments"),
    PRESENTATION("présentation"),
    COMPOSANT("composant");

    private String tableName;
    SchemaNameSpace(String tableName) {
        this.tableName=tableName;
    }

    @Override
    public String toString(){
        return tableName;
    }
}