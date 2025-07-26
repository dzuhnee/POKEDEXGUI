package com.pokedex.app;

import java.util.List;

import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerBasic;

public class AppState {
    private static String selectedTrainerName;
    private static Trainer fullTrainer; // Trainer's full info
    private static TrainerBasic selectedTrainer;

    private static List<Trainer> lastSearchResults;
    private static String lastSearchKeyword;

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

    public static Trainer getFullTrainer() {
        return fullTrainer;
    }

    public static void setFullTrainer(Trainer trainer) {
        fullTrainer = trainer;
    }

    // Set and get last search results
    public static void setLastSearchResults(List<Trainer> results) {
        lastSearchResults = results;
    }

    public static List<Trainer> getLastSearchResults() {
        return lastSearchResults;
    }

    // Set and get last search keyword
    public static void setLastSearchKeyword(String keyword) {
        lastSearchKeyword = keyword;
    }

    public static String getLastSearchKeyword() {
        return lastSearchKeyword;
    }
}
