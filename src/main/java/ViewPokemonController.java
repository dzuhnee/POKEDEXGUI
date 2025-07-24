package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewPokemonController {

    @FXML private TableView<com.pokedex.app.PokemonBasic> pokemonTable;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, Integer> colDex;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, String> colName;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, Integer> colHP;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, Integer> colAttack;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, Integer> colDefense;
    @FXML private TableColumn<com.pokedex.app.PokemonBasic, Integer> colSpeed;

    @FXML
    public void initialize() {
        colDex.setCellValueFactory(new PropertyValueFactory<>("dexNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHP.setCellValueFactory(new PropertyValueFactory<>("hp"));
        colAttack.setCellValueFactory(new PropertyValueFactory<>("attack"));
        colDefense.setCellValueFactory(new PropertyValueFactory<>("defense"));
        colSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));

        pokemonTable.setItems(loadPokemonData());
    }

    private ObservableList<com.pokedex.app.PokemonBasic> loadPokemonData() {
        ObservableList<com.pokedex.app.PokemonBasic> list = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("pokemon_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    int dex = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int hp = Integer.parseInt(parts[7]);
                    int atk = Integer.parseInt(parts[8]);
                    int def = Integer.parseInt(parts[9]);
                    int spd = Integer.parseInt(parts[10]);

                    list.add(new com.pokedex.app.PokemonBasic(dex, name, hp, atk, def, spd));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return list;
    }

    @FXML
    private void handleBack(ActionEvent event) {
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
