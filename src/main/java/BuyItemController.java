package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.pokedex.app.Item;
import com.pokedex.app.Trainer;
import com.pokedex.app.FileUtils;
import com.pokedex.app.ManageTrainerController;
import com.pokedex.app.ItemManager;
import com.pokedex.app.AppState;

public class BuyItemController implements Initializable {

    @FXML private TableView<Item> shopTable;
    @FXML private TableColumn<Item, String> colItemName;
    @FXML private TableColumn<Item, String> colItemDescription;
    @FXML private TableColumn<Item, Integer> colItemPrice;

    @FXML private TextField quantityField;
    @FXML private Label trainerMoneyLabel;
    @FXML private Label trainerNameLabel;
    @FXML private Label feedbackLabel;
    @FXML private Label totalCostLabel;

    private String searchKeyword;
    private Trainer trainer;
    private ObservableList<Item> shopItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trainer = AppState.getFullTrainer();
        if (trainer == null) {
            System.out.println("Trainer not loaded in BuyItemController");
        }

        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));

        initializeShopItems();
        shopTable.setItems(shopItems);

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalCost());
        shopTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTotalCost());

        feedbackLabel.setText("");
        quantityField.setText("1"); // Default
        updateTrainerInfo();        // Display trainer's name and money
    }

    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }

    private void initializeShopItems() {
        shopItems = FXCollections.observableArrayList();
        shopItems.addAll(new ItemManager().getAllItems());
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        updateTrainerInfo();
    }

    private void updateTrainerInfo() {
        if (trainer != null) {
            trainerNameLabel.setText(trainer.getName());
            trainerMoneyLabel.setText("₱" + String.format("%,d", trainer.getMoney()));
        }
    }

    private void updateTotalCost() {
        Item selectedItem = shopTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity > 0) {
                    int totalCost = selectedItem.getBuyingPrice() * quantity;
                    totalCostLabel.setText("Total: ₱" + String.format("%,d", totalCost));
                } else {
                    totalCostLabel.setText("Total: ₱0");
                }
            } catch (NumberFormatException e) {
                totalCostLabel.setText("Total: ₱0");
            }
        } else {
            totalCostLabel.setText("Total: ₱0");
        }
    }

    @FXML
    private void handleBuy() {
        feedbackLabel.setText("");
        feedbackLabel.setStyle("-fx-background-color: transparent;");

        Item selectedItem = shopTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showFeedback("Please select an item to buy.", "error");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showFeedback("Please enter a valid quantity.", "error");
                return;
            }
        } catch (NumberFormatException e) {
            showFeedback("Please enter a valid number.", "error");
            return;
        }

        boolean success = trainer.processPurchase(selectedItem, quantity);
        if (success) {
            // Save updated trainer info to file
            FileUtils.updateTrainerInFile(trainer);

            showFeedback("Purchase successful!", "success");
            updateTrainerInfo();
            quantityField.setText("1");
            updateTotalCost();

            // For debugging only
            System.out.println("Trainer's updated item bag:");
            for (Item item : trainer.getItemBag()) {
                System.out.println("- " + item.getName());
            }

        } else {
            int totalCost = selectedItem.getBuyingPrice() * quantity;
            if (totalCost > trainer.getMoney()) {
                showFeedback("Insufficient funds!", "error");
            } else if (trainer.getItemQuantity(selectedItem.getName()) == 0 &&
                    trainer.getUniqueItemTypeCount() >= 10) {
                showFeedback("Bag full - too many item types!", "error");
            } else if (trainer.getTotalItemCount() + quantity > 50) {
                showFeedback("Bag full - not enough space!", "error");
            } else {
                showFeedback("Purchase failed!", "error");
            }
        }
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTrainer.fxml"));
            Parent root = loader.load();

            ManageTrainerController manageController = loader.getController();
            manageController.setTrainer(trainer);
            manageController.setSearchKeyword(searchKeyword); // if needed

            Stage stage = (Stage) trainerMoneyLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }
    }
}