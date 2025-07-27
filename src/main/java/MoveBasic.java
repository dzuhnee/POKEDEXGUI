package com.pokedex.app;

public class MoveBasic {
    private String name;
    private String description;
    private String type1;
    private String type2;
    private String classification;

    public MoveBasic(String name, String description, String type1, String type2, String classification) {
        this.name = name;
        this.description = description;
        this.type1 = type1;
        this.type2 = type2;
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getClassification() {
        return classification;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }
}
