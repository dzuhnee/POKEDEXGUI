package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.pokedex.app.AppState;
import com.pokedex.app.TrainerBasic;

public class AddPokemonToLineUpController {

    @FXML private ListView<String> pokemonListView;
    @FXML private Label trainerNameLabel;  // changed from TextField to Label

    private final ObservableList<String> displayList = FXCollections.observableArrayList();
    private final Map<String, String> fullPokemonDataMap = new HashMap<>();
    private String trainerName;  // store trainer name internally

    @FXML
    public void initialize() {
        loadPokemonData();
        pokemonListView.setItems(displayList);
    }

    // This method will be called by the previous controller to pass the trainer name
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
        trainerNameLabel.setText(trainerName);
        AppState.setSelectedTrainerName(trainerName); // ✅ use the correct method
    }

    private void loadPokemonData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("pokemon_data.txt"));
            for (String line : lines) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    String displayName = tokens[1] + " (" + tokens[2] + (tokens[3].isEmpty() ? "" : "/" + tokens[3]) + ")";
                    displayList.add(displayName);
                    fullPokemonDataMap.put(displayName, line);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not read pokemon_data.txt");
        }
    }

    @FXML
    public void handleAssignPokemon() {
        String selectedDisplay = pokemonListView.getSelectionModel().getSelectedItem();

        if (selectedDisplay == null || trainerName == null || trainerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "Please select a Pokémon. (Trainer name should be pre-set.)");
            return;
        }

        String fullPokemonLine = fullPokemonDataMap.get(selectedDisplay);
        String assignment = trainerName + " -> " + fullPokemonLine;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainer_lineup.txt", true))) {
            writer.write(assignment);
            writer.newLine();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Write Error", "Failed to save the assignment.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Assigned Pokémon to " + trainerName + "!");
        pokemonListView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTrainer.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
