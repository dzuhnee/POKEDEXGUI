package com.pokedex.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import com.pokedex.app.TeachMoveController;

public class TeachMoveLauncher extends Application {

    private static int trainerIdToUse = 1; // Pass this from ManageTrainer

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TeachMove.fxml"));
        Parent root = loader.load();

        TeachMoveController controller = loader.getController();
        controller.setTrainerId(trainerIdToUse);

        primaryStage.setTitle("Teach Move");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void setTrainerId(int id) {
        trainerIdToUse = id;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
