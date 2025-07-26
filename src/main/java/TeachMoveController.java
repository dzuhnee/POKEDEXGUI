package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TeachMoveController {

    @FXML private ComboBox<String> pokemonComboBox;
    @FXML private ComboBox<String> moveComboBox;
    @FXML private Button teachButton;
    @FXML private Label trainerLabel;
    private String trainerName;
    private Map<String, String> pokedexMap = new HashMap<>(); // displayName → line
    private List<String> moveList = new ArrayList<>();
    private int selectedTrainerId = -1;

    // Call this from ManageTrainer screen before loading this scene
    public void setTrainerId(int trainerId) {
        this.selectedTrainerId = trainerId;
        loadPokemonForTrainer();
    }

    @FXML
    public void initialize() {
        loadMoves();
        moveComboBox.setItems(FXCollections.observableArrayList(moveList));

        teachButton.setOnAction(this::handleTeachMove);
    }

    private void loadPokemonForTrainer() {
        try (BufferedReader br = new BufferedReader(new FileReader("trainer_lineup.txt"))) {
            List<String> displayList = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.contains("->")) continue;

                String[] splitNameAndData = line.split("->");
                if (splitNameAndData.length != 2) continue;

                String trainerNamePart = splitNameAndData[0].trim();
                String dataPart = splitNameAndData[1].trim(); // "1,Bulbasaur"

                String[] parts = dataPart.split(",");
                if (parts.length < 2) continue;

                int trainerId = Integer.parseInt(parts[0].trim());
                String pokemonName = parts[1].trim();

                // Filter by trainer name
                if (!trainerNamePart.equalsIgnoreCase(trainerName)) continue;

                String display = pokemonName + " (Trainer: " + trainerNamePart + ")";
                displayList.add(display);
                pokedexMap.put(display, line);
            }

            pokemonComboBox.setItems(FXCollections.observableArrayList(displayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMoves() {
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    moveList.add(parts[0]); // just the move name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTrainerName(String name) {
        this.trainerName = name;
        if (trainerLabel != null) {
            trainerLabel.setText("Trainer: " + name);
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleTeachMove(ActionEvent event) {
        String selectedPokemon = pokemonComboBox.getValue();
        String selectedMove = moveComboBox.getValue();

        if (selectedPokemon == null || selectedMove == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select both a Pokémon and a move.");
            alert.showAndWait();
            return;
        }


        String line = pokedexMap.get(selectedPokemon);
        System.out.println("Teaching move " + selectedMove + " to " + selectedPokemon);
        // Here you'd update the Pokémon's moveset and write it back to file
        // (omitted for brevity, but I can help you write that too if needed)
    }
}
