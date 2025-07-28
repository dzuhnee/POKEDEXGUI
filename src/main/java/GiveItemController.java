package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.AppState;
import com.pokedex.app.Pokemon;
import com.pokedex.app.ItemRow;
import com.pokedex.app.Item;
import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.FileUtils;

public class GiveItemController {

    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, String> colPokemonName;
    @FXML private TableColumn<Pokemon, String> colHeldItem;

    @FXML private TableView<ItemRow> itemTable;
    @FXML private TableColumn<ItemRow, String> colItemName;
    @FXML private TableColumn<ItemRow, Integer> colItemQuantity;

    @FXML private Label trainerNameLabel;
    @FXML private Label feedbackLabel;

    private Trainer trainer;

    @FXML
    public void initialize() {
        trainer = TrainerManager.loadTrainerByID(AppState.getInstance().getFullTrainer().getTrainerID());
        AppState.getInstance().setFullTrainer(trainer);
        trainer.setLineup(AppState.getInstance().loadLineupFromFile(trainer.getName()));


        trainerNameLabel.setText("Trainer: " + trainer.getName());

        colPokemonName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHeldItem.setCellValueFactory(new PropertyValueFactory<>("heldItemName"));

        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        populatePokemonTable();
        populateItemsTable();
    }

    private void populatePokemonTable() {
        ObservableList<Pokemon> pokemons = FXCollections.observableArrayList(trainer.getLineup());
        pokemonTable.setItems(pokemons);
    }

    private void populateItemsTable() {
        List<Item> bagItems = trainer.getItemBag();
        List<ItemRow> itemRows = new ArrayList<>();

        for (Item item : bagItems) {
            boolean found = false;
            for (int i = 0; i < itemRows.size(); i++) {
                ItemRow row = itemRows.get(i);
                if (row.getName().equals(item.getName())) {
                    // Replace the old row with a new one with incremented quantity
                    itemRows.set(i, new ItemRow(item, row.getQuantity() + 1));
                    found = true;
                    break;
                }
            }
            if (!found) {
                itemRows.add(new ItemRow(item, 1));
            }
        }

        itemTable.setItems(FXCollections.observableArrayList(itemRows));
    }


    @FXML
    public void handleGive() {
        Pokemon selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
        ItemRow selectedItem = itemTable.getSelectionModel().getSelectedItem();

        if (selectedPokemon == null || selectedItem == null) {
            feedbackLabel.setText("Please select both a Pokémon and an item.");
            return;
        }

        if (selectedPokemon.getHeldItem() != null) {
            feedbackLabel.setText(selectedPokemon.getName() + " is already holding an item.");
            return;
        }

        selectedPokemon.setHeldItem(selectedItem.getItem());
        trainer.removeItemFromBag(selectedItem.getName(), 1);

        FileUtils.saveHeldItem(trainer.getTrainerID(), selectedPokemon.getName(), selectedItem.getName());
        FileUtils.updateTrainerItemsInFile(trainer);
        FileUtils.updateTrainerInFile(trainer);

        feedbackLabel.setText(selectedPokemon.getName() + " is now holding " + selectedItem.getName() + ".");
        populateItemsTable();
        pokemonTable.refresh();
    }

    @FXML
    public void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) pokemonTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
