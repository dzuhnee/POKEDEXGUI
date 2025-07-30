package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.pokedex.app.Item;
import com.pokedex.app.ItemRow;
import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.FileUtils;
import com.pokedex.app.AppState;

public class RemoveItemController implements Initializable {

    @FXML private TableView<ItemRow> itemBagTable;
    @FXML private TableColumn<ItemRow, String> colName;
    @FXML private TableColumn<ItemRow, String> colDescription;
    @FXML private TableColumn<ItemRow, Integer> colQuantity;
    @FXML private Label trainerNameLabel;
    @FXML private Label feedbackLabel;

    private Trainer trainer;
    private String searchKeyword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load trainer from AppState
        trainer = AppState.getInstance().getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in RemoveItemController");
            return;
        }

        // Set up table columns
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Enable text wrapping for better display
        enableTextWrap(colName);
        enableTextWrap(colDescription);

        // Display trainer info and populate table
        updateTrainerInfo();
        populateItemBagTable();

        // Clear feedback label initially
        feedbackLabel.setText("");
    }

    // Enables text wrapping for table columns
    private void enableTextWrap(TableColumn<ItemRow, String> column) {
        column.setCellFactory(tc -> {
            TableCell<ItemRow, String> cell = new TableCell<ItemRow, String>() {
                private final javafx.scene.text.Text text = new javafx.scene.text.Text();

                // Constructor instead of initialization block
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                    text.getStyleClass().add("wrapped-text");
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

    // Updates trainer name display
    private void updateTrainerInfo() {
        if (trainer != null) {
            trainerNameLabel.setText("Trainer: " + trainer.getName());
        }
    }

    // Populates the item bag table with grouped items and quantities
    private void populateItemBagTable() {
        List<Item> bagItems = trainer.getItemBag();
        List<ItemRow> itemRows = new ArrayList<>();

        // Group items by name and count quantities
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

        itemBagTable.setItems(FXCollections.observableArrayList(itemRows));
    }

    // Handles removing selected item from trainer's bag
    @FXML
    private void handleRemove() {
        // Clear previous feedback
        feedbackLabel.setText("");

        ItemRow selectedItem = itemBagTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            feedbackLabel.setText("Please select an item to remove.");
            return;
        }

        // If item has quantity > 1, ask how many to remove
        int quantityToRemove = 1;
        if (selectedItem.getQuantity() > 1) {
            quantityToRemove = askForQuantity(selectedItem);
            if (quantityToRemove == -1) {
                return; // User cancelled
            }
        }

        // Confirm removal with alert dialog
        String confirmMessage = "Remove " + quantityToRemove + " " +
                selectedItem.getName() + "(s) from your bag?";
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Item");
        confirmAlert.setContentText(confirmMessage);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove items from trainer's bag
            boolean success = trainer.removeItemFromBag(selectedItem.getName(), quantityToRemove);

            if (success) {
                // Update files and refresh display
                FileUtils.updateTrainerInFile(trainer);

                // Reload trainer to ensure consistency
                Trainer updatedTrainer = TrainerManager.loadTrainerByID(trainer.getTrainerID());
                AppState.getInstance().setFullTrainer(updatedTrainer);
                this.trainer = updatedTrainer;

                populateItemBagTable();

                // Show success in alert instead of feedback label
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText(quantityToRemove + " " + selectedItem.getName() + "(s) removed successfully!");
                successAlert.showAndWait();

                // Clear any previous feedback messages
                feedbackLabel.setText("");
            } else {
                // Show failure in alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to remove item from bag.");
                errorAlert.showAndWait();
            }
        }
    }

    // Asks user how many items to remove when quantity > 1
    private int askForQuantity(ItemRow selectedItem) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Remove Items");
        dialog.setHeaderText("Remove " + selectedItem.getName());
        dialog.setContentText("How many to remove? (Available: " + selectedItem.getQuantity() + ")");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int quantity = Integer.parseInt(result.get());
                if (quantity > 0 && quantity <= selectedItem.getQuantity()) {
                    return quantity;
                } else {
                    feedbackLabel.setText("Please enter a valid quantity between 1 and " + selectedItem.getQuantity());
                    feedbackLabel.setStyle("-fx-text-fill: #dc3545;"); // Error red color
                    return -1;
                }
            } catch (NumberFormatException e) {
                feedbackLabel.setText("Please enter a valid number.");
                feedbackLabel.setStyle("-fx-text-fill: #dc3545;"); // Error red color
                return -1;
            }
        }
        return -1; // User cancelled
    }

    // Navigates back to the ManageTrainer screen
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) itemBagTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setter methods for external controllers
    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        updateTrainerInfo();
        populateItemBagTable();
        feedbackLabel.setText(""); // Clear feedback when trainer changes
    }
}