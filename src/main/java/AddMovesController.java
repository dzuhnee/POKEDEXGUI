package com.pokedex.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/*
 * Declares FXML fields for user input:
 * - pokedexNumberField: for the Pokémon's Pokédex number
 * - pokemonNameField: for the move's name
 * - typeField1: for the first type
 * - typeField2: for the second type
 * - classificationBox: for selecting between TM or HM
 */
public class AddMovesController implements Initializable {

    @FXML private TextField pokedexNumberField;
    @FXML private TextField pokemonNameField;
    @FXML private ComboBox<String> typeField1;
    @FXML private ComboBox<String> typeField2;
    @FXML private ComboBox<String> classificationBox;

    /*
     * Initializes the ComboBox fields with Pokémon type options and classifications.
     * typeField1 includes a "None" option for Pokémon with only one type.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> types = Arrays.asList(
                "Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Fighting",
                "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost",
                "Dragon", "Dark", "Steel", "Fairy"
        );

        // Type 1 gets only actual types
        typeField2.getItems().addAll(types);

        // Type 2 gets "None" + actual types
        typeField1.getItems().add("None");
        typeField1.getItems().addAll(types);

        // Add classification options
        classificationBox.getItems().addAll("TM (Technical Machine)", "HM (Hidden Machine)");
    }

    /*
     * Handles the "Save" button:
     * - Validates if all required fields are filled
     * - Writes input data to moves.txt
     * - Shows success or error alerts
     * - Clears fields after successful save
     */
    @FXML
    private void handleSave() {
        String number = pokedexNumberField.getText();
        String name = pokemonNameField.getText();
        String type1 = typeField1.getValue();
        String type2 = typeField2.getValue();
        String classification = classificationBox.getValue();

        // Show warning if any field is missing
        if (number.isEmpty() || name.isEmpty() || type1 == null || type2 == null || classification == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields required.");
            alert.showAndWait();
            return;
        }

        // Try to save the move data to moves.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("moves.txt", true))) {
            writer.write(number + "," + name + "," + type1 + "," + type2 + "," + classification);
            writer.newLine();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Move successfully saved!");
            alert.showAndWait();

            // Clear fields after saving
            pokedexNumberField.clear();
            pokemonNameField.clear();
            typeField1.setValue(null);
            typeField2.setValue(null);
            classificationBox.setValue(null);

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save move. Please try again.");
            alert.showAndWait();
        }
    }

    /*
     * Handles the "Back" button:
     * - Loads the previous scene (MovesTab.fxml)
     * - Navigates back to the Moves tab interface
     */
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MovesTab.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
