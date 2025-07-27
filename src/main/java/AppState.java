package com.pokedex.app;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Pokemon;
import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerBasic;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.ItemManager;
import com.pokedex.app.FileUtils;
import com.pokedex.app.Item;

public class AppState {
    // Singleton instance
    private static AppState instance;

    private AppState() {}

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

    // ✅ NEW METHOD TO RELOAD TRAINER DATA FROM FILE
    public void reloadTrainerDataFromFile() {
        Trainer current = this.fullTrainer;
        if (current != null) {
            int trainerID = current.getTrainerID();
            Trainer updatedTrainer = TrainerManager.loadTrainerByID(trainerID);

            if (updatedTrainer != null) {
                this.fullTrainer = updatedTrainer;
            }
        }
    }


    public List<Pokemon> loadLineupFromFile(String trainerName) {
        List<Pokemon> lineup = new ArrayList<>();

        try {
            File file = new File("trainer_lineup.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith(trainerName + " -> ")) {
                    String[] parts = line.split("->")[1].trim().split(",");
                    int pokedexNumber = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();

                    Pokemon p = new Pokemon(pokedexNumber, name, "", "", 1, -1, -1, -1, 0, 0, 0, 0);

                    // ✅ Load held item for this Pokémon
                    String heldItemName = FileUtils.loadHeldItem(getFullTrainer().getTrainerID(), name);
                    if (!heldItemName.equals("None")) {
                        ItemManager itemManager = new ItemManager();
                        Item heldItem = itemManager.findItem(heldItemName);
                        if (heldItem != null) {
                            p.setHeldItem(heldItem);
                        }
                    }

                    lineup.add(p);
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lineup;
    }

}
