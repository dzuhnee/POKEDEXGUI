package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

import com.pokedex.app.AppState;
import com.pokedex.app.Trainer;
import com.pokedex.app.Pokemon;
import com.pokedex.app.Item;
import com.pokedex.app.FileUtils;

public class RemoveItemController {

    @FXML private ComboBox<String> modeComboBox;
    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableView<ItemRow> itemTable;
    @FXML private TableColumn<Pokemon, String> pokemonNameColumn;
    @FXML private TableColumn<Pokemon, String> heldItemColumn;
    @FXML private TableColumn<ItemRow, String> itemNameColumn;
    @FXML private TableColumn<ItemRow, Integer> quantityColumn;
    @FXML private Label statusLabel;

    private Trainer trainer;

    @FXML
    public void initialize() {
        modeComboBox.setItems(FXCollections.observableArrayList("Remove from Pokémon", "Remove from Inventory"));
        modeComboBox.setOnAction(e -> switchMode());

        trainer = AppState.getInstance().getFullTrainer();

        // Pokémon table
       // pokemonNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
       // heldItemColumn.setCellValueFactory(cellData -> cellData.getValue().heldItemNameProperty());

        // Item table
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadTables();
    }

    private void switchMode() {
        String selected = modeComboBox.getValue();
        if ("Remove from Pokémon".equals(selected)) {
            pokemonTable.setVisible(true);
            itemTable.setVisible(false);
        } else {
            itemTable.setVisible(true);
            pokemonTable.setVisible(false);
        }
    }

    private void loadTables() {
        pokemonTable.setItems(FXCollections.observableArrayList(trainer.getLineup()));

        Map<String, Integer> itemCounts = new LinkedHashMap<>();
        for (Item item : trainer.getItemBag()) {
            itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
        }

        ObservableList<ItemRow> itemRows = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            itemRows.add(new ItemRow(entry.getKey(), entry.getValue()));
        }

        itemTable.setItems(itemRows);
    }

    @FXML
    private void handleRemove() {
        String selected = modeComboBox.getValue();

        if ("Remove from Pokémon".equals(selected)) {
            Pokemon selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
            if (selectedPokemon == null || selectedPokemon.getHeldItem() == null) {
                statusLabel.setText("No Pokémon or held item selected.");
                return;
            }

            selectedPokemon.setHeldItem(null);
            statusLabel.setText(selectedPokemon.getName() + "'s held item removed.");

        } else if ("Remove from Inventory".equals(selected)) {
            ItemRow selectedItem = itemTable.getSelectionModel().getSelectedItem();
            if (selectedItem == null || selectedItem.getQuantity() <= 0) {
                statusLabel.setText("No item selected or quantity is 0.");
                return;
            }

            trainer.removeItemFromBag(selectedItem.getItemName(), 1); // assumes this method exists
            statusLabel.setText("Removed one " + selectedItem.getItemName() + " from inventory.");
        }

        // Persist changes
        FileUtils.updateTrainerInFile(trainer);
        loadTables();
    }

    @FXML
    private void handleBack() {
        // SceneNavigator.goBack(); // Or however your app navigates
    }

    // Simple helper class for item rows
    public static class ItemRow {
        private final String itemName;
        private final int quantity;

        public ItemRow(String itemName, int quantity) {
            this.itemName = itemName;
            this.quantity = quantity;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
