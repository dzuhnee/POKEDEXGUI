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
import java.util.*;

import com.pokedex.app.Pokemon;
import com.pokedex.app.Item;
import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.FileUtils;
import com.pokedex.app.AppState;

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
        trainer = AppState.getInstance().getFullTrainer();

        if (trainer == null) {
            feedbackLabel.setText("No trainer loaded.");
            return;
        }

        trainerNameLabel.setText("Trainer: " + trainer.getName());

        // ✅ Reload updated lineup from file
        List<Pokemon> lineupFromFile = AppState.getInstance().loadLineupFromFile(trainer.getName());
        trainer.setLineup(lineupFromFile);

        // Set up table columns
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
        List<ItemRow> itemRows = new ArrayList<>();

        for (Item item : trainer.getItemBag()) {
            boolean found = false;
            for (ItemRow row : itemRows) {
                if (row.getName().equals(item.getName())) {
                    row.increaseQuantity();
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

        // ❗ Prevent giving item if Pokémon is already holding something
        if (selectedPokemon.getHeldItem() != null) {
            feedbackLabel.setText(selectedPokemon.getName() + " is already holding an item.");
            return;
        }

        // Set new held item
        selectedPokemon.setHeldItem(selectedItem.getItem());

        // Remove one quantity of item from bag
        trainer.removeItemFromBag(selectedItem.getName(), 1);

        // Save item change
        FileUtils.saveHeldItem(trainer.getTrainerID(), selectedPokemon.getName(), selectedItem.getName());
        FileUtils.updateTrainerItemsInFile(trainer);
        FileUtils.updateTrainerInFile(trainer); // Optional if only money changes

        // Feedback and refresh
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

    public static class ItemRow {
        private Item item;
        private int quantity;

        public ItemRow(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public Item getItem() {
            return item;
        }

        public String getName() {
            return item.getName();
        }

        public int getQuantity() {
            return quantity;
        }

        public void increaseQuantity() {
            quantity++;
        }
    }
}
