package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;
import com.pokedex.app.FileUtils;
import com.pokedex.app.Item;
import com.pokedex.app.ItemManager;


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

    public static Trainer loadTrainerByID(int id) {
        List<String> lines = FileUtils.readFile("trainers.txt");

        int tempID = -1;
        String name = "";
        LocalDate birthdate = null;
        String gender = "";
        String hometown = "";
        String description = "";
        int money = 0;

        Trainer trainer = null; // initialize here so we can populate items

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.startsWith("ID:")) {
                tempID = Integer.parseInt(line.substring(3).trim());
            } else if (line.startsWith("Name:")) {
                name = line.substring(5).trim();
            } else if (line.startsWith("Birthdate:")) {
                birthdate = LocalDate.parse(line.substring(10).trim());
            } else if (line.startsWith("Gender:")) {
                gender = line.substring(7).trim();
            } else if (line.startsWith("Hometown:")) {
                hometown = line.substring(9).trim();
            } else if (line.startsWith("Description:")) {
                description = line.substring(12).trim();
            } else if (line.startsWith("Money:")) {
                money = Integer.parseInt(line.substring(6).trim());
            } else if (line.startsWith("Items:")) {
                // create the trainer object here so we can add items to it
                trainer = new Trainer(tempID, name, birthdate, gender, hometown, description, money);

                String itemsLine = line.substring(6).trim();
                if (!itemsLine.equalsIgnoreCase("None")) {
                    String[] itemNames = itemsLine.split(",");
                    ItemManager im = new ItemManager();
                    for (String itemName : itemNames) {
                        Item item = im.findItem(itemName.trim());
                        if (item != null) {
                            trainer.addItemToBag(item, 1);
                        }
                    }
                }
            } else if (line.startsWith("--------------------------------------------------")) {
                if (trainer != null && trainer.getTrainerID() == id) {
                    return trainer;
                }

                // Reset all fields for next trainer
                tempID = -1;
                name = "";
                birthdate = null;
                gender = "";
                hometown = "";
                description = "";
                money = 0;
                trainer = null;
            }
        }

        return null; // not found
    }



}
