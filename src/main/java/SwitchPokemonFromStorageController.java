package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import com.pokedex.app.AppState;
import com.pokedex.app.Trainer;

public class SwitchPokemonFromStorageController {

    private static final Map<Integer, String> pokemonIdNameMap = new HashMap<>();

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
    private final List<Integer> lineupIDs = new ArrayList<>();
    private final List<Integer> storageIDs = new ArrayList<>();

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
        AppState.setSelectedTrainerName(trainerName);

        if (trainerNameLabel != null) trainerNameLabel.setText("Trainer: " + trainerName);

        loadPokemonData();
        loadTrainerSwitchData();
    }

    @FXML
    private void initialize() {
        if (trainerName == null) {
            trainerName = AppState.getSelectedTrainerName();
        }

        if (trainerName != null && trainerNameLabel != null) {
            trainerNameLabel.setText("Trainer: " + trainerName);
        }

        loadPokemonData();
        loadTrainerSwitchData();


        switchButton.setOnAction(e -> handleSwitch());
        backButton.setOnAction(e -> handleBack());
    }

    private void loadPokemonData() {
        pokemonIdNameMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        pokemonIdNameMap.put(id, name);
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read pokemon_data.txt");
            e.printStackTrace();
        }
    }

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

    private void updateLineupListView() {
        lineupListView.getItems().clear();
        if (lineupIDs.isEmpty()) {
            lineupListView.getItems().add("No Pokémon in lineup.");
        } else {
            for (int id : lineupIDs) {
                lineupListView.getItems().add(id + " - " + pokemonIdNameMap.getOrDefault(id, "Unknown"));
            }
        }
    }

    private void updateStorageListView() {
        storageListView.getItems().clear();

        Set<Integer> used = new HashSet<>(lineupIDs);
        used.addAll(storageIDs);

        Set<Integer> listed = new HashSet<>();
        for (int id : storageIDs) {
            if (listed.add(id) && pokemonIdNameMap.containsKey(id)) {
                storageListView.getItems().add(id + " - " + pokemonIdNameMap.get(id));
            }
        }

        for (Map.Entry<Integer, String> entry : pokemonIdNameMap.entrySet()) {
            if (!used.contains(entry.getKey()) && listed.add(entry.getKey())) {
                storageListView.getItems().add(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

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

    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}