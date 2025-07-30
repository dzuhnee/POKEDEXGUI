package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.pokedex.app.Trainer;

public class ReleasePokemonController {
    @FXML private ListView<String> pokemonListView;
    @FXML private Label trainerLabel;
    private Trainer trainer;

    private String trainerName;
    private final String TRAINER_LINEUP_FILE = "trainer_lineup.txt";
    private String searchKeyword;
    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }

    /*
     * Sets the trainer's name and updates the UI label.
     * Also triggers the loading of that trainer's Pokémon lineup from the file.
     */
    public void setTrainerName(String name) {
        this.trainerName = name;
        trainerLabel.setText("Trainer: " + name);
        loadTrainerPokemon();
    }


    /*
     * Called automatically after the FXML has been loaded.
     * Retrieves the full Trainer object from the shared AppState.
     */
    @FXML
    public void initialize() {
        trainer = com.pokedex.app.AppState.getInstance().getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in ReleasePokemonController");
        }
    }

    /*
     * Reads the trainer_lineup.txt file and finds all Pokémon associated with the current trainer.
     * Populates the ListView with those Pokémon, or shows a placeholder message if none are found.
     */
    private void loadTrainerPokemon() {
        ObservableList<String> trainerPokemons = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_LINEUP_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(trainerName + " -> ")) {
                    String pokemonEntry = line.split("->")[1].trim();
                    trainerPokemons.add(pokemonEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (trainerPokemons.isEmpty()) {
            trainerPokemons.add("No Pokémon to release.");
            pokemonListView.setDisable(true);
        }

        pokemonListView.setItems(trainerPokemons);
    }

    /*
     * Removes the selected Pokémon from the trainer_lineup.txt file,
     * effectively "releasing" it from the trainer's team.
     * Then reloads the updated list of Pokémon.
     */
    @FXML
    private void releasePokemon() {
        String selected = pokemonListView.getSelectionModel().getSelectedItem();
        if (selected == null || selected.equals("No Pokémon to release.")) return;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(TRAINER_LINEUP_FILE));
            List<String> updatedLines = allLines.stream()
                    .filter(line -> !(line.startsWith(trainerName + " -> ") && line.contains(selected)))
                    .collect(Collectors.toList());

            Files.write(Paths.get(TRAINER_LINEUP_FILE), updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadTrainerPokemon();
    }

    /*
     * Handles the "Back" button.
     * Loads the ManageTrainer.fxml scene and replaces the current window content with it.
     */
    @FXML
    private void handleBack(javafx.event.ActionEvent event) {
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
}