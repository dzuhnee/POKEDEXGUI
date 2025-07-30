package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.*;

import com.pokedex.app.AppState;
import com.pokedex.app.Trainer;

public class SwitchPokemonFromStorageController {

    private static final Map<Integer, PokemonInfo> pokemonIdNameMap = new HashMap<>();

    @FXML
    private Label trainerNameLabel;
    @FXML
    private ListView<String> lineupListView;
    @FXML
    private ListView<String> storageListView;
    @FXML
    private Button switchButton;
    @FXML
    private Button backButton;


    private String trainerName;
    private String searchKeyword;
    private final List<Integer> lineupIDs = new ArrayList<>();
    private final List<Integer> storageIDs = new ArrayList<>();
    private Trainer trainer;

    /*
     * Sets the trainer name for this controller, stores it in AppState,
     * updates the label, and loads relevant Pokémon and switch data.
     */
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
        AppState.setSelectedTrainerName(trainerName);

        if (trainerNameLabel != null) trainerNameLabel.setText("Trainer: " + trainerName);

        loadPokemonData();
        loadTrainerSwitchData();
    }

    /*
     * JavaFX lifecycle method called after FXML components are loaded.
     * Ensures trainer data is initialized and event handlers are set.
     */
    @FXML
    private void initialize() {
        if (trainerName == null) {
            trainerName = AppState.getSelectedTrainerName();
        }

        trainer = AppState.getInstance().getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in SwitchPokemonFromStorageController");
        }

        if (trainerName != null && trainerNameLabel != null) {
            trainerNameLabel.setText("Trainer: " + trainerName);
        }

        loadPokemonData();
        loadTrainerSwitchData();


        switchButton.setOnAction(e -> handleSwitch());
    }

    /*
     * Helper class for holding Pokémon display information such as stats.
     */
    public static class PokemonInfo {
        String name;
        int hp, attack, defense, speed;

        public PokemonInfo(String name, int hp, int attack, int defense, int speed) {
            this.name = name;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
        }

        public String toString() {
            return String.format("%s (HP: %d, ATK: %d, DEF: %d, SPD: %d)", name, hp, attack, defense, speed);
        }
    }

    /*
     * Loads Pokémon data from 'pokemon_data.txt' and populates the pokemonIdNameMap.
     * Used for displaying detailed Pokémon information.
     */
    private void loadPokemonData() {
        pokemonIdNameMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        int hp = Integer.parseInt(data[8].trim());
                        int attack = Integer.parseInt(data[9].trim());
                        int defense = Integer.parseInt(data[10].trim());
                        int speed = Integer.parseInt(data[11].trim());

                        pokemonIdNameMap.put(id, new PokemonInfo(name, hp, attack, defense, speed));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read pokemon_data.txt");
            e.printStackTrace();
        }
    }

    /*
     * Loads Pokémon IDs from 'trainer_lineup.txt' for the selected trainer.
     * Populates lineupIDs and storageIDs for lineup and storage Pokémon.
     */
    private void loadTrainerSwitchData() {
        lineupIDs.clear();
        storageIDs.clear();

        Set<Integer> seen = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("trainer_lineup.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(trainerName + " ->")) {
                    String[] parts = line.split("->");
                    if (parts.length == 2) {
                        String[] stats = parts[1].split(",");
                        if (stats.length >= 1) {
                            try {
                                int id = Integer.parseInt(stats[0].trim());
                                if (!seen.add(id)) continue;
                                if (lineupIDs.size() < 6) {
                                    lineupIDs.add(id);
                                } else {
                                    storageIDs.add(id);
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateLineupListView();
        updateStorageListView();
    }

    /*
     * Populates the lineup ListView with Pokémon info from lineupIDs.
     */
    private void updateLineupListView() {
        lineupListView.getItems().clear();
        if (lineupIDs.isEmpty()) {
            lineupListView.getItems().add("No Pokémon in lineup.");
        } else {
            for (int id : lineupIDs) {
                PokemonInfo info = pokemonIdNameMap.get(id);
                if (info != null) {
                    lineupListView.getItems().add(id + " - " + info.toString());
                } else {
                    lineupListView.getItems().add(id + " - Unknown");
                }
            }
        }
    }

    /*
     * Populates the storage ListView with Pokémon info from storageIDs
     * and other Pokémon not currently in lineup.
     */
    private void updateStorageListView() {
        storageListView.getItems().clear();

        Set<Integer> used = new HashSet<>(lineupIDs);
        used.addAll(storageIDs);
        Set<Integer> listed = new HashSet<>();

        for (int id : storageIDs) {
            if (listed.add(id)) {
                PokemonInfo info = pokemonIdNameMap.get(id);
                if (info != null) {
                    storageListView.getItems().add(id + " - " + info.toString());
                }
            }
        }

        for (Map.Entry<Integer, PokemonInfo> entry : pokemonIdNameMap.entrySet()) {
            int id = entry.getKey();
            if (!used.contains(id) && listed.add(id)) {
                storageListView.getItems().add(id + " - " + entry.getValue().toString());
            }
        }
    }

    /*
     * Handles switching a selected Pokémon from the lineup with one from storage.
     * Updates the lists and saves the changes to file.
     */
    private void handleSwitch() {
        int lineupIndex = lineupListView.getSelectionModel().getSelectedIndex();
        int storageIndex = storageListView.getSelectionModel().getSelectedIndex();

        if (lineupIndex < 0 || storageIndex < 0 || lineupIndex >= lineupIDs.size()) return;

        String storageSelected = storageListView.getItems().get(storageIndex);
        int storageId = Integer.parseInt(storageSelected.split(" - ")[0]);

        int lineupId = lineupIDs.get(lineupIndex);

        lineupIDs.set(lineupIndex, storageId);
        storageIDs.remove((Integer) storageId);
        storageIDs.add(lineupId);

        updateLineupListView();
        updateStorageListView();
        saveTrainerSwitchData();
    }

    /*
     * Saves the current trainer's lineup to 'trainer_lineup.txt' after a switch.
     * Uses a temporary file for safe file replacement.
     */
    private void saveTrainerSwitchData() {
        File inputFile = new File("trainer_lineup.txt");
        File tempFile = new File("trainer_lineup_temp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(trainerName + " ->")) {
                    writer.println(line);
                }
            }

            Map<Integer, String> pokemonIdNameMap = new HashMap<>();

            for (int id : lineupIDs) {
                String name = pokemonIdNameMap.getOrDefault(id, "Unknown");
                writer.println(trainerName + " -> " + id + "," + name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.err.println("Failed to update trainer_lineup.txt");
        }
    }

    /*
     * Stores the search keyword for filtering or navigation, if needed.
     */
    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }

    /*
     * Navigates back to the ManageTrainer view, restoring the trainer and search context.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTrainer.fxml"));
            Parent root = loader.load();

            com.pokedex.app.ManageTrainerController manageController = loader.getController();
            manageController.setTrainer(trainer);
            manageController.setSearchKeyword(searchKeyword); // if needed

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handles releasing (deleting) a selected Pokémon from storage.
     * Removes it from memory, storage list, and updates 'pokemon_data.txt'.
     */
    @FXML
    private void handleRelease() {
        int storageIndex = storageListView.getSelectionModel().getSelectedIndex();
        if (storageIndex < 0 || storageIndex >= storageListView.getItems().size()) return;

        String selectedItem = storageListView.getItems().get(storageIndex);
        int selectedId = Integer.parseInt(selectedItem.split(" - ")[0]);

        // Remove from memory
        storageIDs.remove((Integer) selectedId);
        pokemonIdNameMap.remove(selectedId);

        // Remove from pokemon_data.txt
        File dataFile = new File("pokemon_data.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(selectedId + ",")) {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading pokemon_data.txt: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to pokemon_data.txt: " + e.getMessage());
        }

        updateStorageListView();
        saveTrainerSwitchData();
    }


}