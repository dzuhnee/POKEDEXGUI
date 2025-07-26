package com.pokedex.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.pokedex.app.TrainerBasic;
import jdk.swing.interop.SwingInterOpUtils;
import com.pokedex.app.AddPokemonToLineUpController;
import com.pokedex.app.SwitchPokemonFromStorageController;
import com.pokedex.app.AppState;

import com.pokedex.app.Trainer;
import com.pokedex.app.ReleasePokemonController;
import com.pokedex.app.TeachMoveController;



public class ManageTrainerController {

    private Trainer trainer;

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    private String searchKeyword;

    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPokemonToLineUp.fxml"));
            Parent root = loader.load();

            AddPokemonToLineUpController controller = loader.getController();
            controller.setTrainerName(trainer.getName());

            // ✅ Store globally so it's remembered later (e.g., Switch screen)
            AppState.setSelectedTrainerName(trainer.getName());

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
        if (trainer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Trainer Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a trainer first.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SwitchPokemonFromStorage.fxml"));
            Parent root = loader.load();

            // ✅ Store globally so Switch controller can access it
            TrainerBasic basicTrainer = new TrainerBasic(
                    trainer.getTrainerID(),
                    trainer.getName(),
                    trainer.getBirthdate(),
                    trainer.getGender(),
                    trainer.getHometown(),
                    trainer.getDescription()
            );
            AppState.setSelectedTrainer(basicTrainer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Switch Pokemon From Storage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReleasePokemon(ActionEvent event) {
        if (trainer == null) {
            System.out.println("No trainer selected!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReleasePokemon.fxml"));
            Parent root = loader.load();

            // Pass selected trainer
            ReleasePokemonController controller = loader.getController();
            controller.setTrainerName(trainer.getName()); // this is where the trainer is passed

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTeachMove(ActionEvent event) {
        if (trainer == null) {
            System.out.println("No trainer selected!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TeachMove.fxml"));
            Parent root = loader.load();

            // Pass selected trainer
            TeachMoveController controller = loader.getController();
            controller.setTrainerName(trainer.getName()); // this is where the trainer is passed
            controller.setTrainerId(trainer.getTrainerID());


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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