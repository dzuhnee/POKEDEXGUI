package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;

// This class allows the trainers to be accessed and modified globally -- will edit the other controllers soon <3
public class TrainerManager {

    // Shared trainer list
    private static ObservableList<Trainer> allTrainers = FXCollections.observableArrayList();

    public static ObservableList<Trainer> getAllTrainers() {
        return allTrainers;
    }

    public static void setTrainers(ObservableList<Trainer> trainers) {
        allTrainers = trainers;
    }

    public static void addTrainer(Trainer trainer) {
        allTrainers.add(trainer);
    }

    public static Trainer findByName(String name) {
        for (Trainer t : allTrainers) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    public static Trainer getTrainerByID(int id) {
        for (Trainer t : allTrainers) {
            if (t.getTrainerID() == id) {
                return t;
            }
        }
        return null;
    }

    // Do we need to clear?
    public static void clearAll() {
        allTrainers.clear();
    }

    // Search for trainers based on any keyword
    public static List<Trainer> searchTrainers(String keyword) {
        List<Trainer> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Trainer t : allTrainers) {
            if (t.getName().toLowerCase().contains(lowerKeyword) ||
                    t.getGender().toLowerCase().contains(lowerKeyword) ||
                    t.getBirthdate().toString().toLowerCase().contains(lowerKeyword) ||
                    String.valueOf(t.getTrainerID()).contains(lowerKeyword) ||
                    t.getDescription().toLowerCase().contains(lowerKeyword) ||
                    t.getHometown().toLowerCase().contains(lowerKeyword)) {

                matches.add(t);
            }
        }
        return matches;
    }

    // Preload trainers on app start
    public static void populateInitialTrainers() {
        if (allTrainers.isEmpty()) {
            addTrainer(new Trainer(1, "Ash Ketchum", LocalDate.of(1987, 5, 22),
                    "Male", "Pallet Town", "Pokemon Trainer from Kanto", 1_000_000));

            addTrainer(new Trainer(2, "Misty", LocalDate.of(1988, 3, 18),
                    "Female", "Cerulean City", "Cerulean Gym Leader", 1_000_000));

            addTrainer(new Trainer(3, "Brock", LocalDate.of(1985, 9, 15),
                    "Male", "Pewter City", "Former Pewter Gym Leader", 1_000_000));

            addTrainer(new Trainer(4, "Gary Oak", LocalDate.of(1987, 11, 22),
                    "Male", "Pallet Town", "Pokemon Researcher", 1_000_000));
        }
    }

}
