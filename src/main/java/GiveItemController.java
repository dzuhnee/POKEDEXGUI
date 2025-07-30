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

    // Pokemon Table
    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, String> colPokemonName;
    @FXML private TableColumn<Pokemon, String> colHeldItem;

    // Item Inventory Table
    @FXML private TableView<ItemRow> itemTable;
    @FXML private TableColumn<ItemRow, String> colItemName;
    @FXML private TableColumn<ItemRow, Integer> colItemQuantity;

    @FXML private Label trainerNameLabel;
    @FXML private Label feedbackLabel;

    private Trainer trainer;

    // Initializes the Give Item function's UI
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

    // Populates the Pokemon table with the trainer's active lineup and their held item
    private void populatePokemonTable() {
        ObservableList<Pokemon> pokemons = FXCollections.observableArrayList(trainer.getLineup());
        pokemonTable.setItems(pokemons);
    }

    // Populates the Item Inventory Table with the trainer's stored items and their quantity
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

    // Handles giving items from trainer's bag to selected Pokémon
    @FXML
    public void handleGive() {
        Pokemon selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
        ItemRow selectedItem = itemTable.getSelectionModel().getSelectedItem();

        // User must select a Pokémon and an item from their respective tables
        if (selectedPokemon == null || selectedItem == null) {
            feedbackLabel.setText("Please select both a Pokémon and an item.");
            return;
        }

        // If Pokémon already holds an item, confirm replacement with user
        if (selectedPokemon.getHeldItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Replace Item");
            alert.setHeaderText(selectedPokemon.getName() + " is already holding " + selectedPokemon.getHeldItemName());
            alert.setContentText("Do you want to replace it with " + selectedItem.getName() + "?");

            // Exit if user cancels the replacement
            if (alert.showAndWait().get() != ButtonType.OK) {
                return;
            }

            // Return the currently held item back to trainer's bag
            trainer.addItemToBag(selectedPokemon.getHeldItem(), 1);
        }

        // Give the selected item to the Pokémon and remove it from trainer's bag
        selectedPokemon.setHeldItem(selectedItem.getItem());
        trainer.removeItemFromBag(selectedItem.getName(), 1);

        // Save changes to files and update UI
        FileUtils.saveHeldItem(trainer.getTrainerID(), selectedPokemon.getName(), selectedItem.getName());
        FileUtils.updateTrainerItemsInFile(trainer);
        FileUtils.updateTrainerInFile(trainer);

        // Update feedback and reflect changes
        feedbackLabel.setText(selectedPokemon.getName() + " is now holding " + selectedItem.getName() + ".");
        populateItemsTable();
        pokemonTable.refresh();
    }

    // Returns to the previous Manage Trainer screen
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
