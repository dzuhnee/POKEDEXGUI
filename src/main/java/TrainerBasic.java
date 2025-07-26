package com.pokedex.app;

import java.time.LocalDate;

public class TrainerBasic {
    private int trainerID;
    private String name;
    private LocalDate birthdate;
    private String sex;
    private String hometown;
    private String description;

    // Full constructor
    public TrainerBasic(int trainerID, String name, LocalDate birthdate, String sex, String hometown, String description) {
        this.trainerID = trainerID;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.hometown = hometown;
        this.description = description;
    }

    // Minimal constructor for name-only use
    public TrainerBasic(String name) {
        this.name = name;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getSex() {
        return sex;
    }

    public String getHometown() {
        return hometown;
    }

    public String getDescription() {
        return description;
    }
}
