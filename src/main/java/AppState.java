package com.pokedex.app;


import com.pokedex.app.TrainerBasic;

public class AppState {
    private static String selectedTrainerName;

    public static void setSelectedTrainerName(String name) {
        selectedTrainerName = name;
    }

    public static String getSelectedTrainerName() {
        return selectedTrainerName;
    }
    private static TrainerBasic selectedTrainer;

    public static void setSelectedTrainer(TrainerBasic trainer) {
        selectedTrainer = trainer;
    }

    public static TrainerBasic getSelectedTrainer() {
        return selectedTrainer;
    }
}

