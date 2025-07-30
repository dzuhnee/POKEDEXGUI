package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.*;

import com.pokedex.app.PokemonBasic;

public class SearchPokemonController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TableView<PokemonBasic> resultTable;
    @FXML private TableColumn<PokemonBasic, Integer> colDex;
    @FXML private TableColumn<PokemonBasic, String> colName;
    @FXML private TableColumn<PokemonBasic, Integer> colHP;
    @FXML private TableColumn<PokemonBasic, Integer> colAttack;
    @FXML private TableColumn<PokemonBasic, Integer> colDefense;
    @FXML private TableColumn<PokemonBasic, Integer> colSpeed;
    @FXML private TableColumn<PokemonBasic, String> colMoves;


    private final ObservableList<PokemonBasic> allPokemon = FXCollections.observableArrayList();
    private final Map<PokemonBasic, List<String>> typeMap = new HashMap<>();

    /*
     * Initializes the controller by setting up table column mappings,
     * loading Pokémon data and types from file, and preparing the type dropdown.
     */
    @FXML
    public void initialize() {
        colDex.setCellValueFactory(new PropertyValueFactory<>("dexNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHP.setCellValueFactory(new PropertyValueFactory<>("hp"));
        colAttack.setCellValueFactory(new PropertyValueFactory<>("attack"));
        colDefense.setCellValueFactory(new PropertyValueFactory<>("defense"));
        colSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        colMoves.setCellValueFactory(new PropertyValueFactory<>("moves"));

        loadPokemonDataFromFile();
        initializeTypeComboBox();
        resultTable.setVisible(false);
    }

    /*
     * Populates the type combo box with all possible Pokémon types
     * and sets the default value to "-- Select Type --".
     */
    private void initializeTypeComboBox() {
        List<String> allTypes = Arrays.asList(
                "Normal", "Fire", "Water", "Electric", "Grass", "Ice",
                "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug",
                "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
        );

        ObservableList<String> typeOptions = FXCollections.observableArrayList();
        typeOptions.add("-- Select Type --");
        typeOptions.addAll(allTypes);

        typeComboBox.setItems(typeOptions);
        typeComboBox.setValue("-- Select Type --");
    }

    /*
     * Loads additional moves per Pokémon from "pokemon_moves.txt" file.
     * Returns a map where key = Pokémon name and value = comma-separated moves.
     */
    private Map<String, String> loadPokemonMoves() {
        Map<String, String> movesMap = new HashMap<>();
        File file = new File("pokemon_moves.txt");

        if (!file.exists()) return movesMap;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String moves = parts[1].trim();
                    movesMap.put(name, moves);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movesMap;
    }

    /*
     * Loads all Pokémon data from "pokemon_data.txt" and adds them to the observable list.
     * Also loads their corresponding moves and maps each Pokémon to their types for filtering.
     */
    private void loadPokemonDataFromFile() {
        File file = new File("pokemon_data.txt");
        if (!file.exists()) {
            System.err.println("File not found: pokemon_data.txt");
            return;
        }

        Map<String, String> movesMap = loadPokemonMoves();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 12) continue;

                try {
                    int dex = Integer.parseInt(tokens[0].trim());
                    String name = tokens[1].trim();
                    List<String> types = List.of(tokens[2].trim(), tokens[3].trim());
                    int hp = Integer.parseInt(tokens[8].trim());
                    int attack = Integer.parseInt(tokens[9].trim());
                    int defense = Integer.parseInt(tokens[10].trim());
                    int speed = Integer.parseInt(tokens[11].trim());

                    PokemonBasic p = new PokemonBasic(dex, name, hp, attack, defense, speed);

                    // Attach moves if available
                    String defaultMoves = "Tackle, Defend";
                    if (movesMap.containsKey(name)) {
                        p.setMoves(defaultMoves + ", " + movesMap.get(name));
                    } else {
                        p.setMoves(defaultMoves);
                    }


                    allPokemon.add(p);
                    typeMap.put(p, types);
                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Skipping malformed line: " + line);
                }
            }

            System.out.println("Loaded " + allPokemon.size() + " Pokémon.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handles the search button click.
     * Filters Pokémon based on the entered keyword and selected type.
     * Displays the results in the table.
     */
    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedType = typeComboBox.getValue();

        ObservableList<PokemonBasic> filteredList = FXCollections.observableArrayList();

        for (PokemonBasic p : allPokemon) {
            boolean matchesKeyword = keyword.isEmpty()
                    || p.getName().toLowerCase().contains(keyword)
                    || String.valueOf(p.getDexNumber()).equals(keyword);

            boolean matchesType = selectedType == null
                    || selectedType.equals("-- Select Type --")
                    || typeMap.get(p).contains(selectedType);

            if (matchesKeyword && matchesType) {
                filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()) {
            System.out.println("No data found for that search.");
        }

        resultTable.setItems(filteredList);
        resultTable.setVisible(true);
    }

    /*
     * Handles the "Back" button.
     * Loads the Pokémon tab scene to return to the previous menu.
     */
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PokemonTab.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}