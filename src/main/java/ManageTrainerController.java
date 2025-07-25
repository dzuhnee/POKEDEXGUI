package com.pokedex.app;

import com.pokedex.app.BuyItemController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import jdk.swing.interop.SwingInterOpUtils;
import com.pokedex.app.AddPokemonToLineUpController;

public class ManageTrainerController {

    private Trainer selectedTrainer;
    private String searchKeyword;

    public void setTrainer(Trainer trainer) {
        this.selectedTrainer = trainer;
    }
    public void setSearchKeyword(String keyword) { this.searchKeyword = keyword; }

    // UNCOMMENT LATER
    @FXML
    public void handleBuyItem(ActionEvent event) {
        navigateToScreen("/BuyItem.fxml", event);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPokemonToLineUp.fxml"));
            Parent root = loader.load();

            AddPokemonToLineUpController controller = loader.getController();
            controller.setTrainerName(selectedTrainer.getName());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Pokémon to Lineup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrainerResults.fxml"));
            Parent root = loader.load();

            TrainerResultsController resultsController = loader.getController();
            resultsController.performSearch(searchKeyword);

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
            if (controller instanceof BuyItemController) {
                BuyItemController buyController = (BuyItemController) controller;
                buyController.setTrainer(selectedTrainer);
                buyController.setSearchKeyword(searchKeyword);
            }
            /* UNCOMMENT ME LATER
            else if (controller instanceof SellItemController) {
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