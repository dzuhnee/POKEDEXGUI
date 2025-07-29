package com.pokedex.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/*
 * Declares FXML UI components:
 * - Text fields for inputting Pokémon details such as Pokédex number, name, base level, evolution info, and stats.
 * - ComboBoxes for selecting type1 and type2.
 */
public class AddPokemonController {

    @FXML private TextField pokedexNumberField;
    @FXML private TextField pokemonNameField;
    @FXML private ComboBox<String> typeField;
    @FXML private ComboBox<String> type2Field;
    @FXML private TextField baseLevelField;
    @FXML private TextField evolvesFromField;
    @FXML private TextField evolvesToField;
    @FXML private TextField evolutionLevelField;
    @FXML private TextField hpField;
    @FXML private TextField atkField;
    @FXML private TextField defField;
    @FXML private TextField spdField;

    /*
     * Initializes the type selection ComboBoxes with all possible Pokémon types.
     * Also sets a default prompt for the base level input.
     */
    @FXML
    public void initialize() {
        List<String> types = Arrays.asList(
                "Normal", "Fire", "Water", "Electric", "Grass", "Ice",
                "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug",
                "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
        );

        typeField.getItems().addAll(types);
        type2Field.getItems().addAll(types);

        baseLevelField.setPromptText("Enter base level default: 5");
    }

    /*
     * Handles the "Save" button click:
     * - Validates if all required fields are filled and contain valid input
     * - Checks for duplicate Pokédex numbers or Pokémon names
     * - Writes the new Pokémon entry to pokemon_data.txt if all checks pass
     */
    @FXML
    private void handleSave(ActionEvent event) {
        if (isAnyFieldEmpty()) {
            showAlert("Please fill in all required fields!");
            return;
        }

        if (!isInteger(pokedexNumberField.getText()) || !isInteger(baseLevelField.getText()) ||
                !isInteger(evolutionLevelField.getText()) ||
                !isInteger(hpField.getText()) || !isInteger(atkField.getText()) ||
                !isInteger(defField.getText()) || !isInteger(spdField.getText())) {
            showAlert("Level and stats must be valid numbers!");
            return;
        }

        int pokedexNumber = Integer.parseInt(pokedexNumberField.getText());
        int evolutionLevel = Integer.parseInt(evolutionLevelField.getText());

        if (pokedexNumber < 1 || pokedexNumber > 1010) {
            showAlert("Pokedex number must be between 1 and 1010.");
            return;
        }

        if (evolutionLevel < 2) {
            showAlert("Evolution level must be at least 2.");
            return;
        }

        if (isDuplicate(pokedexNumberField.getText(), pokemonNameField.getText())) {
            showAlert("Duplicate Pokedex number or Pokémon name detected!");
            return;
        }

        if (evolvesFromField.getText().trim().equalsIgnoreCase(evolvesToField.getText().trim())) {
            showAlert("A Pokémon cannot evolve from and to the same Pokémon!");
            return;
        }

        // Save data
        String data = String.join(",",
                pokedexNumberField.getText(),
                pokemonNameField.getText(),
                typeField.getValue(),
                type2Field.getValue(),
                baseLevelField.getText(),
                evolutionLevelField.getText(),
                evolvesFromField.getText(),
                evolvesToField.getText(),
                hpField.getText(),
                atkField.getText(),
                defField.getText(),
                spdField.getText()
        );

        try {
            FileWriter txtWriter = new FileWriter("pokemon_data.txt", true);
            txtWriter.write(data + "\n");
            txtWriter.close();
            System.out.println("Pokémon saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error saving Pokémon data.");
        }
    }

    /*
     * Checks if any of the required input fields are empty.
     * Returns true if any field is blank; otherwise false.
     */
    private boolean isAnyFieldEmpty() {
        return pokedexNumberField.getText().isEmpty() ||
                pokemonNameField.getText().isEmpty() ||
                typeField.getValue() == null ||
                type2Field.getValue() == null ||
                baseLevelField.getText().isEmpty() ||
                evolutionLevelField.getText().isEmpty() ||
                evolvesFromField.getText().isEmpty() ||
                evolvesToField.getText().isEmpty() ||
                hpField.getText().isEmpty() ||
                atkField.getText().isEmpty() ||
                defField.getText().isEmpty() ||
                spdField.getText().isEmpty();
    }

    /*
     * Utility method to check if a string can be parsed as an integer.
     * Returns true if parsable; otherwise false.
     */
    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Checks for existing entries in pokemon_data.txt that match
     * the same Pokédex number or Pokémon name to avoid duplicates.
     */
    private boolean isDuplicate(String pokedexNumber, String pokemonName) {
        File file = new File("pokemon_data.txt");
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    if (parts[0].equals(pokedexNumber) || parts[1].equalsIgnoreCase(pokemonName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Handles the "Back" button click:
     * - Loads the previous screen (PokemonTab.fxml)
     * - Navigates back to the main Pokémon tab interface
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PokemonTab.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Displays an error alert dialog with the given message.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
