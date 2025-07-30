package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.pokedex.app.Item;
import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.AppState;
import com.pokedex.app.FileUtils;
import com.pokedex.app.ItemRow;

public class SellItemController {

    @FXML private TableView<ItemRow> bagTable;
    @FXML private TableColumn<ItemRow, String> colItemName;
    @FXML private TableColumn<ItemRow, String> colItemDescription;
    @FXML private TableColumn<ItemRow, Integer> colItemPrice;

    @FXML private TextField quantityField;
    @FXML private Label trainerMoneyLabel;
    @FXML private Label trainerNameLabel;
    @FXML private Label feedbackLabel;
    @FXML private Label saleValueLabel;

    private ObservableList<ItemRow> displayedItems;

    /*
     * Initializes the controller.
     * Sets up table columns, loads trainer info, and populates the item bag table.
     */
    @FXML
    public void initialize() {
        Trainer currentTrainer = AppState.getInstance().getFullTrainer();

        quantityField.setPromptText("1");

        setupTableColumns();
        updateTrainerInfo();
        loadBagTable();
    }

    /*
     * Configures the columns of the TableView to bind item properties.
     * Also enables text wrapping for the description column.
     */
    private void setupTableColumns() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        enableTextWrap(colItemDescription);
    }

    /*
     * Updates the trainer's displayed name and money from the current AppState.
     */
    private void updateTrainerInfo() {
        Trainer trainer = AppState.getInstance().getFullTrainer();
        if (trainer != null) {
            trainerNameLabel.setText(trainer.getName());
            trainerMoneyLabel.setText("₱" + trainer.getMoney());
        }
    }

    /*
     * Loads and groups items from the trainer's bag into ItemRow objects.
     * Displays them in the TableView.
     */
    private void loadBagTable() {
        Trainer trainer = AppState.getInstance().getFullTrainer();
        if (trainer == null) return;

        List<Item> bag = trainer.getItemBag();
        List<ItemRow> rows = new ArrayList<>();

        for (int i = 0; i < bag.size(); i++) {
            Item current = bag.get(i);
            String currentName = current.getName();
            boolean alreadyCounted = false;

            // Check if already added
            for (ItemRow row : rows) {
                if (row.getName().equals(currentName)) {
                    alreadyCounted = true;
                    break;
                }
            }

            if (!alreadyCounted) {
                int count = 0;
                for (Item item : bag) {
                    if (item.getName().equals(currentName)) {
                        count++;
                    }
                }
                rows.add(new ItemRow(current, count));
            }
        }
        displayedItems = FXCollections.observableArrayList(rows);
        bagTable.setItems(displayedItems);
    }

    /*
     * Handles the logic for selling an item.
     * Validates input, updates trainer's inventory and money, and refreshes display.
     */
    @FXML
    private void handleSell(ActionEvent event) {
        Trainer trainer = AppState.getInstance().getFullTrainer();
        if (trainer == null) return;

        ItemRow selected = bagTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Please select an item to sell.", "error");
            return;
        }

        quantityField.setPromptText("1"); // test
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) {
                showFeedback("Quantity cannot be (less than) 0.", "error");
                return;
            }
        } catch (NumberFormatException e) {
            showFeedback("Invalid quantity.", "error");
            return;
        }

        if (selected.getQuantity() < quantity) {
            showFeedback("You only have " + selected.getQuantity() + ".", "error");
            return;
        }

        Item item = selected.getItem();
        int totalValue = item.getSellingPrice() * quantity;

        boolean success = trainer.processSale(item, quantity);
        if (success) {
            FileUtils.updateTrainerInFile(trainer);

            Trainer updatedTrainer = TrainerManager.loadTrainerByID(trainer.getTrainerID());
            AppState.getInstance().setFullTrainer(updatedTrainer);
            trainer = updatedTrainer;

            showFeedback("Sold " + quantity + " " + item.getName() + "(s)!", "success");
            saleValueLabel.setText("₱" + totalValue);
            updateTrainerInfo();
            loadBagTable(); // reload table with new data
            quantityField.clear();
        } else {
            showFeedback("Sale failed.", "error");
        }
    }

    /*
     * Navigates back to the ManageTrainer screen.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageTrainer.fxml"));
            Stage stage = (Stage) bagTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Displays a styled message in the feedback label.
     * Message style depends on the type: success, error, or neutral.
     */
    private void showFeedback(String message, String type) {
        feedbackLabel.setText(message);

        if (type.equals("success")) {
            feedbackLabel.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;" +
                    "-fx-padding: 5px; -fx-border-color: #c3e6cb; -fx-border-radius: 3px;" +
                    "-fx-background-radius: 3px;");
        } else if (type.equals("error")) {
            feedbackLabel.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;" +
                    "-fx-padding: 5px; -fx-border-color: #f5c6cb; -fx-border-radius: 3px;" +
                    "-fx-background-radius: 3px;");
        } else {
            feedbackLabel.setStyle("");
        }
    }

    /*
     * Enables text wrapping for a given TableColumn to properly display long descriptions.
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

        // test
    }
}
