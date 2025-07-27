package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.*;

import com.pokedex.app.Pokemon;
import com.pokedex.app.Item;
import com.pokedex.app.Trainer;
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

        colPokemonName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHeldItem.setCellValueFactory(new PropertyValueFactory<>("heldItemName"));

        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadPokemonTable();
        loadItemsTable();
    }

    private void loadPokemonTable() {
        pokemonTable.getItems().clear();
        pokemonTable.getItems().addAll(trainer.getLineup());
    }

    private void loadItemsTable() {
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

        selectedPokemon.setHeldItem(selectedItem.getItem());

        // Remove one instance of the item from the bag
        for (Iterator<Item> iterator = trainer.getItemBag().iterator(); iterator.hasNext(); ) {
            if (iterator.next().getName().equals(selectedItem.getName())) {
                iterator.remove();
                break;
            }
        }

        feedbackLabel.setText(selectedPokemon.getName() + " is now holding " + selectedItem.getName() + ".");

        loadPokemonTable();
        loadItemsTable();

        FileUtils.updateTrainerInFile(trainer);
    }

    @FXML
    public void handleBack() {
        Stage stage = (Stage) trainerNameLabel.getScene().getWindow();
        stage.close();
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
