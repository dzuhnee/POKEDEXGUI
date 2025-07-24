package com.pokedex.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.pokedex.app.TrainerBasic;
import jdk.swing.interop.SwingInterOpUtils;

public class ManageTrainerController {

    private TrainerBasic selectedTrainer;

    public void setTrainer(TrainerBasic trainer) {
        this.selectedTrainer = trainer;
    }

    // UNCOMMENT LATER
    @FXML
    public void handleBuyItem(ActionEvent event) {
        System.out.println("Buy Item clicked!");
        // navigateToScreen("/BuyItem.fxml", event);
    }

    @FXML
    public void handleSellItem(ActionEvent event) {
        System.out.println("Sell Item clicked");
        // navigateToScreen("/SellItem.fxml", event);
    }

    @FXML
    public void handleUseItem(ActionEvent event) {
        System.out.println("Use Item clicked!");
        // navigateToScreen("/UseItem.fxml", event);
    }

    @FXML
    public void handleGiveItem(ActionEvent event) {
        System.out.println("Give Item clicked!");
        // navigateToScreen("/GiveItem.fxml", event);
    }

    @FXML
    public void handleRemoveItem(ActionEvent event) {
        System.out.println("Remove Item clicked!");
        // navigateToScreen("/RemoveItem.fxml", event);
    }

    @FXML
    public void handleAddToLineup(ActionEvent event) {
        System.out.println("Add to Lineup clicked");
        // navigateToScreen("/AddToLineup.fxml", event);
    }

    @FXML
    public void handleSwitchPokemon(ActionEvent event) {
        System.out.println("Switch Pokemon clicked!");
        // navigateToScreen("/SwitchPokemon.fxml", event);
    }

    @FXML
    public void handleReleasePokemon(ActionEvent event) {
        System.out.println("Release Pokemon clicked!");
        // navigateToScreen("/ReleasePokemon.fxml", event);
    }

    @FXML
    public void handleTeachMove(ActionEvent event) {
        System.out.println("Teach Move clicked!");
        // navigateToScreen("/TeachMove.fxml", event);
    }

    @FXML
    public void handleViewProfile(ActionEvent event) {
        System.out.println("View Profile clicked!");
        // navigateToScreen("/ViewProfile.fxml", event);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/TrainerResults.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method for the handle methods - navigate to screens and pass trainer data
    private void navigateToScreen(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();

            // Pass trainer to all controllers that need it
            /* UNCOMMENT ME LATER
            if (controller instanceof BuyItemController) {
                ((BuyItemController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof SellItemController) {
                ((SellItemController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof UseItemController) {
                ((UseItemController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof GiveItemController) {
                ((GiveItemController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof RemoveItemController) {
                ((RemoveItemController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof AddPokemonController) {
                ((AddPokemonController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof SwitchPokemonController) {
                ((SwitchPokemonController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof ReleasePokemonController) {
                ((ReleasePokemonController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof TeachMoveController) {
                ((TeachMoveController) controller).setTrainer(selectedTrainer);
            } else if (controller instanceof ViewProfileController) {
                ((ViewProfileController) controller).setTrainer(selectedTrainer);
            }
            */

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}