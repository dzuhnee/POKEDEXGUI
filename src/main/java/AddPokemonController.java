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

    @FXML
    public void initialize() {
        List<String> types = Arrays.asList(
                "Normal", "Fire", "Water", "Electric", "Grass", "Ice",
                "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug",
                "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
        );

        typeField.getItems().addAll(types);
        type2Field.getItems().addAll(types);
    }

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

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
