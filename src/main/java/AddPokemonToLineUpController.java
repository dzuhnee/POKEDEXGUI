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
import com.pokedex.app.Trainer;

public class AddPokemonToLineUpController {

    /*
     * UI components:
     * - pokemonListView: List of available Pokémon to assign
     * - trainerNameLabel: Displays the current trainer's name
     */
    @FXML private ListView<String> pokemonListView;
    @FXML private Label trainerNameLabel;  // changed from TextField to Label
    private String trainerName; // store trainer name internally

    /*
     * displayList: Observable list for the ListView UI
     * fullPokemonDataMap: Maps display name to full Pokémon data line
     * trainerName: Stores the trainer's name
     * trainer: The currently selected Trainer object from AppState
     */
    private final ObservableList<String> displayList = FXCollections.observableArrayList();
    private final Map<String, String> fullPokemonDataMap = new HashMap<>();
    private Trainer trainer;
    private String searchKeyword;

    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }

    /*
     * Called automatically when the FXML view is loaded.
     * - Loads Pokémon data from file
     * - Sets the ListView and trainer label
     */
    @FXML
    public void initialize() {
        loadPokemonData();
        pokemonListView.setItems(displayList);
        if (trainerName == null) {
            trainerName = AppState.getSelectedTrainerName();
        }

        trainer = AppState.getInstance().getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in AddToPokemonLineUpController");
        }

        if (trainerName != null && trainerNameLabel != null) {
            trainerNameLabel.setText("Trainer: " + trainerName);
        }
    }

    /*
     * Allows another controller to set the trainer name.
     * - Updates label and AppState with selected trainer
     */
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
        trainerNameLabel.setText(trainerName);
        AppState.setSelectedTrainerName(trainerName);
    }

    /*
     * Loads Pokémon data from pokemon_data.txt.
     * - Populates displayList with formatted Pokémon info
     * - Maps display name to original file line for later access
     */
    private void loadPokemonData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("pokemon_data.txt"));
            for (String line : lines) {
                String[] tokens = line.split(",");
                if (tokens.length >= 12) {  // must include up to Speed
                    String displayName = tokens[1] + " (" + tokens[2] +
                            (tokens[3].isEmpty() ? "" : "/" + tokens[3]) +
                            ") - HP: " + tokens[8] +
                            ", Atk: " + tokens[9] +
                            ", Def: " + tokens[10] +
                            ", Spd: " + tokens[11];
                    displayList.add(displayName);
                    fullPokemonDataMap.put(displayName, line);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not read pokemon_data.txt");
        }
    }

    /*
     * Assigns selected Pokémon to a trainer:
     * - Checks for missing input, duplicate entries, or max lineup (6 Pokémon)
     * - Saves assignment to trainer_lineup.txt
     */
    @FXML
    public void handleAssignPokemon() {
        String selectedDisplay = pokemonListView.getSelectionModel().getSelectedItem();

        if (selectedDisplay == null || trainerName == null || trainerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Info", "Please select a Pokémon. (Trainer name should be pre-set.)");
            return;
        }

        String fullPokemonLine = fullPokemonDataMap.get(selectedDisplay);
        String[] tokens = fullPokemonLine.split(",");
        if (tokens.length < 2) {
            showAlert(Alert.AlertType.ERROR, "Data Error", "Invalid Pokémon data format.");
            return;
        }

        String trimmedLine = tokens[0] + "," + tokens[1];  // Only ID and name
        String assignment = trainerName + " -> " + trimmedLine;

        // Check if trainer already has 6 Pokémon OR if this specific assignment already exists
        int count = 0;
        boolean isDuplicate = false;
        try {
            List<String> lines = Files.readAllLines(Paths.get("trainer_lineup.txt"));
            for (String line : lines) {
                if (line.startsWith(trainerName + " ->")) {
                    count++;
                    if (line.equals(assignment)) {
                        isDuplicate = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Read Error", "Failed to read trainer_lineup.txt.");
            return;
        }

        if (isDuplicate) {
            showAlert(Alert.AlertType.WARNING, "Duplicate", "This Pokémon is already assigned to " + trainerName + ".");
            return;
        }

        if (count >= 6) {
            showAlert(Alert.AlertType.WARNING, "Limit Reached", trainerName + " already has 6 Pokémon in their lineup.");
            return;
        }

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

    /*
     * Displays an alert with the given type, title, and message.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
     * Navigates back to the Manage Trainer screen when "Back" button is clicked.
     */
    @FXML
    private void handleBackButton(ActionEvent event) {
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
