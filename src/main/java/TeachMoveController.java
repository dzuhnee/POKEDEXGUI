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

import com.pokedex.app.Trainer;

public class TeachMoveController {

    @FXML private ComboBox<String> pokemonComboBox;
    @FXML private ComboBox<String> moveComboBox;
    @FXML private ComboBox<String> forgetMoveComboBox;
    @FXML private Button teachButton;
    @FXML private Button forgetButton;
    @FXML private Label trainerLabel;

    private String trainerName;
    private Map<String, String> pokedexMap = new HashMap<>();
    private List<String> moveList = new ArrayList<>();
    private int selectedTrainerId = -1;
    private Trainer trainer;

    /*
     * Sets the selected trainer ID and loads their Pokémon.
     */
    public void setTrainerId(int trainerId) {
        this.selectedTrainerId = trainerId;
        loadPokemonForTrainer();
    }

    /*
     * Initializes the controller, loads available moves and sets up button actions.
     */
    @FXML
    public void initialize() {
        loadMoves();
        moveComboBox.setItems(FXCollections.observableArrayList(moveList));
        teachButton.setOnAction(this::handleTeachMove);
        forgetButton.setOnAction(this::handleForgetMove);

        pokemonComboBox.setOnAction(e -> updateForgetMoves());

        trainer = com.pokedex.app.AppState.getInstance().getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in TeachMoveController");
        }
    }

    /*
     * Loads Pokémon lineup for the selected trainer from trainer_lineup.txt.
     */
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
                String dataPart = splitNameAndData[1].trim();

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

    /*
     * Loads available moves from moves.txt and formats them for display.
     * Tackle and Defend are default HM moves — excluded from teachable list.
     */
    private void loadMoves() {
        moveList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    if (name.equalsIgnoreCase("Tackle") || name.equalsIgnoreCase("Defend")) {
                        continue; //  skip default moves
                    }

                    String type = parts[2].trim();
                    String category = parts[3].trim();
                    String classification = parts[4].trim();
                    String shortClass = classification.contains("TM") ? "TM" : "HM";

                    String displayName = name + " (" + type + " / " + category + ") - " + shortClass;
                    moveList.add(displayName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Sets the trainer name and updates the trainer label.
     */
    public void setTrainerName(String name) {
        this.trainerName = name;
        if (trainerLabel != null) {
            trainerLabel.setText("Trainer: " + name);
        }
    }

    /*
     * Handles navigation back to the ManageTrainer screen.
     */
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

        String pokemonName = selectedPokemon.split(" ")[0].trim();
        String moveName = selectedMove.split(" \\(")[0].trim();

        String type1 = null, type2 = null;
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
            showAlert("File Error", "Could not read pokemon file.");
            return;
        }

        if (type1 == null) {
            showAlert("Pokémon Not Found", "Could not find types for " + pokemonName);
            return;
        }

        String moveType = null;
        boolean isHM = false;
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(moveName)) {
                    moveType = parts[2].trim();
                    isHM = parts[4].toUpperCase().contains("HM");
                    break;
                }
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not read moves file.");
            return;
        }

        if (!moveType.equalsIgnoreCase(type1) && !moveType.equalsIgnoreCase(type2)) {
            showAlert("Invalid Type Match", pokemonName + " (" + type1 + "/" + type2 + ") can't learn " + moveName + " (" + moveType + ").");
            return;
        }

        Map<String, List<String>> pokemonMovesMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String[] moves = parts[1].split(",");
                    List<String> moveList = new ArrayList<>();
                    for (String move : moves) moveList.add(move.trim());
                    pokemonMovesMap.put(name, moveList);
                }
            }
        } catch (IOException ignored) {}

        List<String> currentMoves = pokemonMovesMap.getOrDefault(pokemonName, new ArrayList<>());
        if (currentMoves.contains(moveName)) {
            showAlert("Already Knows Move", pokemonName + " already knows " + moveName);
            return;
        }

        long moveCount = currentMoves.size();
        if (moveCount >= 2 && !isHM) {
            showAlert("Too Many Moves", pokemonName + " already knows 4 moves. Please forget one first.");
            return;
        }

        currentMoves.add(moveName);
        pokemonMovesMap.put(pokemonName, currentMoves);

        try (java.io.PrintWriter writer = new java.io.PrintWriter("pokemon_moves.txt")) {
            for (Map.Entry<String, List<String>> entry : pokemonMovesMap.entrySet()) {
                writer.println(entry.getKey() + ": " + String.join(", ", entry.getValue()));
            }
        } catch (IOException e) {
            showAlert("Save Error", "Failed to save move to file.");
            return;
        }

        updateForgetMoves();
        showAlert("Success", pokemonName + " has successfully learned " + moveName + "!");
    }


    /*
     * Handles removing a move from the selected Pokémon.
     */
    private void handleForgetMove(ActionEvent event) {
        String selectedPokemon = pokemonComboBox.getValue();
        String selectedMove = forgetMoveComboBox.getValue();

        if (selectedPokemon == null || selectedMove == null) {
            showAlert("Incomplete Selection", "Please select a Pokémon and a move to forget.");
            return;
        }

        String pokemonName = selectedPokemon.split(" ")[0].trim();

        boolean isHM = false;
        try (BufferedReader br = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(selectedMove)) {
                    isHM = parts[4].toUpperCase().contains("HM");
                    break;
                }
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not read moves.txt.");
            return;
        }

        if (isHM) {
            showAlert("HM Restriction", "HM moves cannot be forgotten.");
            return;
        }

        Map<String, List<String>> pokemonMovesMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    List<String> moves = new ArrayList<>();
                    for (String move : parts[1].split(",")) {
                        moves.add(move.trim());
                    }
                    pokemonMovesMap.put(name, moves);
                }
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not read pokemon_moves.txt.");
            return;
        }

        List<String> currentMoves = pokemonMovesMap.getOrDefault(pokemonName, new ArrayList<>());
        if (!currentMoves.remove(selectedMove)) {
            showAlert("Move Not Found", selectedMove + " not found for " + pokemonName);
            return;
        }

        try (java.io.PrintWriter writer = new java.io.PrintWriter("pokemon_moves.txt")) {
            for (Map.Entry<String, List<String>> entry : pokemonMovesMap.entrySet()) {
                writer.println(entry.getKey() + ": " + String.join(", ", entry.getValue()));
            }
        } catch (IOException e) {
            showAlert("Save Error", "Failed to update move list.");
            return;
        }

        updateForgetMoves();
        showAlert("Move Forgotten", pokemonName + " forgot " + selectedMove + ".");
    }

    /*
     * Updates the forgetMoveComboBox with current moves of the selected Pokémon,
     * excluding default HM moves like Tackle and Defend.
     */
    private void updateForgetMoves() {
        String selected = pokemonComboBox.getValue();
        if (selected == null) return;

        String pokemonName = selected.split(" ")[0].trim();

        List<String> moveSet = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("pokemon_moves.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(pokemonName)) {
                    for (String move : parts[1].split(",")) {
                        String trimmed = move.trim();
                        if (!trimmed.equalsIgnoreCase("Tackle") && !trimmed.equalsIgnoreCase("Defend")) {
                            moveSet.add(trimmed);
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        forgetMoveComboBox.setItems(FXCollections.observableArrayList(moveSet));
    }

    /*
     * Utility method to show a simple information alert to the user.
     */
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}