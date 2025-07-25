package com.pokedex.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import  com.pokedex.app.SwitchPokemonFromStorageController;

public class SwitchPokemonFromStorageLauncher extends Application {

    private static String trainerName;

    public static void setTrainerName(String name) {
        trainerName = name;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SwitchPokemonFromStorage.fxml"));
        Parent root = loader.load();

        SwitchPokemonFromStorageController controller = loader.getController();
        controller.setTrainerName(trainerName);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Switch Pokémon from Storage");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
