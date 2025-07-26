package com.pokedex.app;

import java.util.List;

import com.pokedex.app.TrainerBasic;
import com.pokedex.app.Trainer;

public class AppState {
    // Singleton instance
    private static AppState instance;

    // Private constructor to prevent manual creation
    private AppState() {}

    // Access the ONE AppState object
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    // Static section - for ella
    private static String selectedTrainerName;
    private static TrainerBasic selectedTrainer;

    public static void setSelectedTrainerName(String name) {
        selectedTrainerName = name;
    }

    public static String getSelectedTrainerName() {
        return selectedTrainerName;
    }

    public static void setSelectedTrainer(TrainerBasic trainer) {
        selectedTrainer = trainer;
    }

    public static TrainerBasic getSelectedTrainer() {
        return selectedTrainer;
    }

    // Instance section - for kyle
    private Trainer fullTrainer;
    private List<Trainer> lastSearchResults;
    private String lastSearchKeyword;

    public Trainer getFullTrainer() {
        return fullTrainer;
    }

    public void setFullTrainer(Trainer trainer) {
        this.fullTrainer = trainer;
    }

    public List<Trainer> getLastSearchResults() {
        return lastSearchResults;
    }

    public void setLastSearchResults(List<Trainer> results) {
        this.lastSearchResults = results;
    }

    public String getLastSearchKeyword() {
        return lastSearchKeyword;
    }

    public void setLastSearchKeyword(String keyword) {
        this.lastSearchKeyword = keyword;
    }
}
