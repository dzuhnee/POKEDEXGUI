package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyStringWrapper;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.io.IOException;


import com.pokedex.app.Pokemon;
import com.pokedex.app.PokemonManager;
import com.pokedex.app.Item;
import com.pokedex.app.FileUtils;
import com.pokedex.app.Trainer;
import com.pokedex.app.AppState;
import com.pokedex.app.ItemRow;
import com.pokedex.app.TrainerManager;

public class UseItemController {
    @FXML private Label trainerNameLabel;

    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, String> pokemonNameColumn;
    @FXML private TableColumn<Pokemon, Integer> pokemonLevelColumn;
    @FXML private TableColumn<Pokemon, String> heldItemColumn;

    @FXML private TableView<ItemRow> itemTable;
    @FXML private TableColumn<ItemRow, String> itemNameColumn;
    @FXML private TableColumn<ItemRow, String> itemEffectColumn;
    @FXML private TableColumn<ItemRow, Integer> itemQuantityColumn;

    @FXML private Label infoLabel;

    private Trainer currentTrainer;
    private ItemRow selectedItem;
    private Pokemon selectedPokemon;

    public void initialize() {
        Trainer trainer = TrainerManager.loadTrainerByID(AppState.getInstance().getFullTrainer().getTrainerID());
        AppState.getInstance().setFullTrainer(trainer);
        if (trainer != null && trainerNameLabel != null) {
            trainerNameLabel.setText(trainer.getName());
        }

        pokemonNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pokemonLevelColumn.setCellValueFactory(new PropertyValueFactory<>("baseLevel"));
        heldItemColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getHeldItemName())
        );

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemEffectColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        enableTextWrap(itemEffectColumn);

        pokemonTable.setOnMouseClicked(this::handlePokemonSelection);
        itemTable.setOnMouseClicked(this::handleItemSelection);

        currentTrainer = AppState.getInstance().getFullTrainer();
        loadTables();
    }

    private void loadTables() {
        ObservableList<Pokemon> pokemons = FXCollections.observableArrayList(currentTrainer.getLineup());
        pokemonTable.setItems(pokemons);

        ObservableList<ItemRow> items = FXCollections.observableArrayList(FileUtils.getItemRows(currentTrainer));
        itemTable.setItems(items);
    }

    private void handlePokemonSelection(MouseEvent event) {
        selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
        updateInfoBox();
    }

    private void handleItemSelection(MouseEvent event) {
        selectedItem = itemTable.getSelectionModel().getSelectedItem();
        updateInfoBox();
    }

    private void updateInfoBox() {
        if (selectedPokemon != null && selectedItem != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Using ").append(selectedItem.getName())
                    .append(" on ").append(selectedPokemon.getName()).append("\n\n");

            sb.append("Current Stats:\n")
                    .append("HP: ").append(selectedPokemon.getHP()).append("\n")
                    .append("ATK: ").append(selectedPokemon.getAttack()).append("\n")
                    .append("DEF: ").append(selectedPokemon.getDefense()).append("\n")
                    .append("SPD: ").append(selectedPokemon.getSpeed()).append("\n\n");

            // 🔽 Clone the Pokémon and simulate the item effect on it
            Pokemon simulated = selectedPokemon.cloneForPreview();
            selectedItem.getItem().use(simulated, AppState.getInstance().getPokemonManager());

            // 🔽 Append the "after" stats
            sb.append("After Use:\n")
                    .append("HP: ").append(selectedPokemon.getHP())
                    .append(" → ").append(simulated.getHP()).append("\n")
                    .append("ATK: ").append(selectedPokemon.getAttack())
                    .append(" → ").append(simulated.getAttack()).append("\n")
                    .append("DEF: ").append(selectedPokemon.getDefense())
                    .append(" → ").append(simulated.getDefense()).append("\n")
                    .append("SPD: ").append(selectedPokemon.getSpeed())
                    .append(" → ").append(simulated.getSpeed()).append("\n\n");

            // 🔽 Optional: if your item includes more details like evolution messages
            String extraInfo = selectedItem.getItem().getPreviewEffect(selectedPokemon, AppState.getInstance().getPokemonManager());
            sb.append(extraInfo);

            infoLabel.setText(sb.toString());
        } else {
            infoLabel.setText("");
        }
    }





    @FXML
    private void handleUse(ActionEvent event) {
        if (selectedPokemon == null || selectedItem == null) {
            infoLabel.setText("Please select both a Pokémon and an item.");
            return;
        }

        // Use the item and get the result message
        String resultMessage = selectedPokemon.useItem(selectedItem.getItem(), AppState.getInstance().getPokemonManager());

        // Remove one instance of the used item from the bag
        currentTrainer.removeItemFromBag(selectedItem.getName(), 1);

        // Update trainer items file (for trainer_items.txt)
        FileUtils.updateTrainerItemsInFile(currentTrainer);

        // Update main trainer file (trainers.txt)
        FileUtils.updateTrainerInFile(currentTrainer);

        // Show success and effect message
        infoLabel.setText(resultMessage);

        // Reload tables to reflect changes
        loadTables();
    }



    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) itemTable.getScene().getWindow(); // bag table to fx:id ng inventory
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableTextWrap(TableColumn<ItemRow, String> column) {
        column.setCellFactory(tc -> {
            TableCell<ItemRow, String> cell = new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty || item == null ? "" : item);
                }
            };
            return cell;
        });

        // test
    }
}
