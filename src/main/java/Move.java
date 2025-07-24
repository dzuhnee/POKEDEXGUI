package com.pokedex.app;

public class Move {
    private String name;
    private String description;
    private String type1;
    private String type2;
    private String classification;

    public Move(String name, String description, String type1, String type2, String classification) {
        this.name = name;
        this.description = description;
        this.type1 = type1;
        this.type2 = type2;
        this.classification = classification;
    }

    // ✅ ADD THIS CONSTRUCTOR:
    public Move(String name, String description, String classification) {
        this.name = name;
        this.description = description;
        this.classification = classification;
        this.type1 = "";
        this.type2 = "";
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
}
