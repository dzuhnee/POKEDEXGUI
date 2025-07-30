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

    /*
     * Initializes the UseItemController UI.
     * - Loads trainer data and sets label.
     * - Binds table columns to Pokemon and ItemRow properties.
     * - Enables text wrapping on item columns.
     * - Adds selection listeners for both tables.
     * - Loads current data into tables.
     */
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
        enableTextWrap(itemNameColumn);

        pokemonTable.setOnMouseClicked(this::handlePokemonSelection);
        itemTable.setOnMouseClicked(this::handleItemSelection);

        currentTrainer = AppState.getInstance().getFullTrainer();
        loadTables();

        itemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedItem = newSel;
        });

    }

    /*
     * Displays a warning alert popup with the specified title and content.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*
     * Loads the current trainer’s Pokémon lineup and item inventory into the table views.
     * Called after item use or evolution to reflect updated data.
     */
    private void loadTables() {
        // Updated trainer
        this.currentTrainer = AppState.getInstance().getFullTrainer();

        // After evolving
        ObservableList<Pokemon> pokemons = FXCollections.observableArrayList(
                currentTrainer.getLineup()
        );
        pokemonTable.setItems(pokemons);

        // After using item
        ObservableList<ItemRow> items = FXCollections.observableArrayList(
                FileUtils.getItemRows(currentTrainer)
        );
        itemTable.setItems(items);
    }

    /*
     * Triggered when a Pokémon is selected in the table.
     * - Updates the selectedPokemon variable.
     * - Updates the info box to reflect potential item effect.
     */
    private void handlePokemonSelection(MouseEvent event) {
        selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
        updateInfoBox();
    }

    /*
     * Triggered when an item is selected in the table.
     * - Updates the selectedItem variable.
     * - Updates the info box to reflect potential item effect.
     */
    private void handleItemSelection(MouseEvent event) {
        selectedItem = itemTable.getSelectionModel().getSelectedItem();
        updateInfoBox();
    }

    /*
     * Updates the info box to preview the effects of using the selected item
     * on the selected Pokémon.
     * - Shows current stats.
     * - Simulates the item usage and shows resulting stats.
     * - Appends any special messages or evolution notices.
     */
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

            // Clone the Pokémon and simulate the item effect on it
            Pokemon simulated = selectedPokemon.cloneForPreview();
            selectedItem.getItem().use(simulated, AppState.getInstance().getPokemonManager());

            // Append the "after" stats
            sb.append("After Use:\n")
                    .append("HP: ").append(selectedPokemon.getHP())
                    .append(" → ").append(simulated.getHP()).append("\n")
                    .append("ATK: ").append(selectedPokemon.getAttack())
                    .append(" → ").append(simulated.getAttack()).append("\n")
                    .append("DEF: ").append(selectedPokemon.getDefense())
                    .append(" → ").append(simulated.getDefense()).append("\n")
                    .append("SPD: ").append(selectedPokemon.getSpeed())
                    .append(" → ").append(simulated.getSpeed()).append("\n\n");

            // Optional: if your item includes more details like evolution messages
            String extraInfo = selectedItem.getItem().getPreviewEffect(selectedPokemon, AppState.getInstance().getPokemonManager());
            sb.append(extraInfo);

            infoLabel.setText(sb.toString());
        } else {
            infoLabel.setText("");
        }
    }

    /*
     * Handles the actual item usage logic when the "Use" button is pressed.
     * - Verifies that both a Pokémon and item are selected.
     * - Handles special evolution (e.g., Thunder Stone for Pikachu).
     * - Uses the item and updates stats and trainer data.
     * - Refreshes the table views and UI.
     * - Displays a detailed message of the result.
     */
    @FXML
    private void handleUse(ActionEvent event) {
        if (selectedPokemon == null || selectedItem == null) {
            infoLabel.setText("Please select both a Pokémon and an item.");
            return;
        }

        String itemName = selectedItem.getName();

        // Special case: Thunder Stone triggers evolution manually
        if (itemName.equals("Thunder Stone")) {
            if (selectedPokemon.getName().equals("Pikachu")) {
                // Evolve Pikachu → Raichu
                selectedPokemon.setBaseLevel(11);
                pokemonTable.refresh();
                showAlert("Evolution Success!", "Pikachu evolved into Raichu!");
            } else {
                showAlert("Cannot use Thunder Stone", "This item can only be used on Pikachu.");
                return;
            }
        }

        // Store Pokémon position
        int pokemonPosition = currentTrainer.getLineup().indexOf(selectedPokemon);

        // Use the item (can be Rare Candy etc.)
        String resultMessage = selectedPokemon.useItem(selectedItem.getItem(), AppState.getInstance().getPokemonManager());

        FileUtils.updateTrainerItemsInFile(currentTrainer);
        FileUtils.updateTrainerInFile(currentTrainer);

        loadTables();
        pokemonTable.refresh();
        itemTable.refresh();

        infoLabel.setText(resultMessage);
        pokemonTable.getSelectionModel().clearSelection();
        itemTable.getSelectionModel().clearSelection();

        boolean itemHadEffect = !resultMessage.toLowerCase().contains("no effect") &&
                !resultMessage.toLowerCase().contains("cannot be used") &&
                !resultMessage.toLowerCase().contains("has no effect");

        if (itemHadEffect) {
            currentTrainer.removeItemFromBag(itemName, 1);

            // Just reuse selectedPokemon — it’s already updated
            Pokemon updatedPokemon = selectedPokemon;

            if (pokemonPosition >= 0 && updatedPokemon != null) {
                currentTrainer.getLineup().set(pokemonPosition, updatedPokemon);
            }

            AppState.getInstance().setFullTrainer(currentTrainer);
            updateInfoBox();

            StringBuilder enhancedMessage = new StringBuilder();
            enhancedMessage.append(resultMessage).append("\n\n");
            enhancedMessage.append("Updated Stats:\n")
                    .append("Name: ").append(updatedPokemon.getName()).append("\n")
                    .append("Level: ").append(updatedPokemon.getBaseLevel()).append("\n")
                    .append("HP: ").append(updatedPokemon.getHP()).append("\n")
                    .append("ATK: ").append(updatedPokemon.getAttack()).append("\n")
                    .append("DEF: ").append(updatedPokemon.getDefense()).append("\n")
                    .append("SPD: ").append(updatedPokemon.getSpeed()).append("\n");

            infoLabel.setText(enhancedMessage.toString());

            try {
                FileUtils.updateTrainerInFile(currentTrainer); // not required but fine to keep
            } catch (Exception e) {
                System.out.println("File save error: " + e.getMessage());
            }
        } else {
            infoLabel.setText(resultMessage);
        }

        selectedPokemon = null;
        selectedItem = null;
        pokemonTable.getSelectionModel().clearSelection();
        itemTable.getSelectionModel().clearSelection();
        loadTables();
    }

    /*
     * Returns to the ManageTrainer screen when "Back" button is clicked.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) itemTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Enables text wrapping for long strings in the specified table column.
     * Useful for item names or descriptions that span multiple lines.
     */
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
    }
}
