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
import com.pokedex.app.PokemonBasic;

import java.io.IOException;

public class SearchPokemonController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<PokemonBasic> resultTable;

    @FXML
    private TableColumn<PokemonBasic, Integer> colDex;

    @FXML
    private TableColumn<PokemonBasic, String> colName;

    @FXML
    private TableColumn<PokemonBasic, Integer> colHP;

    @FXML
    private TableColumn<PokemonBasic, Integer> colAttack;

    @FXML
    private TableColumn<PokemonBasic, Integer> colDefense;

    @FXML
    private TableColumn<PokemonBasic, Integer> colSpeed;

    private final ObservableList<PokemonBasic> allPokemon = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind columns
        colDex.setCellValueFactory(new PropertyValueFactory<>("dexNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHP.setCellValueFactory(new PropertyValueFactory<>("hp"));
        colAttack.setCellValueFactory(new PropertyValueFactory<>("attack"));
        colDefense.setCellValueFactory(new PropertyValueFactory<>("defense"));
        colSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));

        // Sample data
        allPokemon.addAll(
                new PokemonBasic(1, "Bulbasaur", 45, 49, 49, 45),
                new PokemonBasic(4, "Charmander", 39, 52, 43, 65),
                new PokemonBasic(7, "Squirtle", 44, 48, 65, 43)
        );

        resultTable.setVisible(false); // Hide table initially
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();

        ObservableList<PokemonBasic> filteredList = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            filteredList.addAll(allPokemon); // Show all if empty
        } else {
            for (PokemonBasic p : allPokemon) {
                if (p.getName().toLowerCase().contains(keyword)) {
                    filteredList.add(p);
                }
            }
        }

        resultTable.setItems(filteredList);
        resultTable.setVisible(true); // Show table only after search
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PokemonTab.fxml")); // adjust path if needed
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
