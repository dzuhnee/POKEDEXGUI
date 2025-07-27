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
        try (BufferedReader br = new BufferedReader(new FileReader("trainer_lineup.txt"));
             BufferedReader pokedexReader = new BufferedReader(new FileReader("pokemon_data.txt"))) {

            Map<String, String[]> typeMap = new HashMap<>();
            String pokedexLine;
            while ((pokedexLine = pokedexReader.readLine()) != null) {
                String[] parts = pokedexLine.split(",");
                if (parts.length >= 4) {
                    String name = parts[1].trim();
                    String type1 = parts[2].trim();
                    String type2 = parts[3].trim();
                    typeMap.put(name.toLowerCase(), new String[]{type1, type2});
                }
            }

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

                if (!trainerNamePart.equalsIgnoreCase(trainerName)) continue;

                String[] types = typeMap.getOrDefault(pokemonName.toLowerCase(), new String[]{"Unknown", "None"});
                String display = pokemonName + " - " + types[0] + (types[1].equalsIgnoreCase("None") ? "" : "/" + types[1]);
                displayList.add(display);
                pokedexMap.put(display, line);
            }

            pokemonComboBox.setItems(FXCollections.observableArrayList(displayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMoves() {
        moveList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String type = parts[2].trim();         // elemental type like Fire
                    String category = parts[3].trim();     // Physical, Special, Status
                    String classification = parts[4].trim(); // TM or HM (full string)

                    // Optional: clean classification to just "TM" or "HM"
                    String shortClass = classification.contains("TM") ? "TM" : "HM";

                    // 👇 Show both type + category
                    String displayName = name + " (" + type + " / " + category + ") - " + shortClass;
                    moveList.add(displayName);
                } else {
                    System.out.println("Skipping invalid move line: " + line);
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
            showAlert("Incomplete Selection", "Please select both a Pokémon and a move.");
            return;
        }

        // Extract just the Pokémon name
        String pokemonName = selectedPokemon.split(" ")[0].trim();

        // Extract just the move name
        String moveName = selectedMove.split(" \\(")[0].trim();

        // Get Pokémon types
        String type1 = null;
        String type2 = null;
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equalsIgnoreCase(pokemonName)) {
                    type1 = parts[2].trim();
                    type2 = parts[3].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("File Error", "Could not read pokemon file.");
            return;
        }

        if (type1 == null) {
            showAlert("Pokémon Not Found", "Could not find types for " + pokemonName);
            return;
        }

        // Get move's elemental type (column 2 in moves.txt)
        String moveType = null;
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(moveName)) {
                    moveType = parts[2].trim();  // ✅ Correct column for move type
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("File Error", "Could not read moves file.");
            return;
        }

        if (moveType == null) {
            showAlert("Move Not Found", "Could not find type for move " + moveName);
            return;
        }

        // ✅ Check for match
        if (!moveType.equalsIgnoreCase(type1) && !moveType.equalsIgnoreCase(type2)) {
            showAlert("Invalid Type Match", pokemonName + " (" + type1 + "/" + type2 + ") can't learn " + moveName + " (" + moveType + ").");
            return;
        }

        // ✅ Success!
        System.out.println("Teaching move " + moveName + " to " + pokemonName);
        showAlert("Success", pokemonName + " has successfully learned " + moveName + "!");
    }


    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
