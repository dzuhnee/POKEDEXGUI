package com.pokedex.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private StackPane rootPane;

    @FXML
    private Group mainGroup;

    @FXML
    private Button pokemonBtn;

    @FXML
    private Button movesBtn;

    @FXML
    private Button itemsBtn;

    @FXML
    private Button trainersBtn;

    private final double designWidth = 600;
    private final double designHeight = 400;

    @FXML
    public void initialize() {
        // Responsive scaling when window is resized
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> scaleGroup());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> scaleGroup());

        // Initial scaling
        scaleGroup();
    }

    private void scaleGroup() {
        double scaleX = rootPane.getWidth() / designWidth;
        double scaleY = rootPane.getHeight() / designHeight;

        mainGroup.setScaleX(scaleX);
        mainGroup.setScaleY(scaleY);
        mainGroup.setTranslateX(0);
        mainGroup.setTranslateY(0);
    }

    @FXML
    private void handlePokemon(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PokemonTab.fxml"));
            Parent trainerRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene trainerScene = new Scene(trainerRoot);
            stage.setScene(trainerScene);
            stage.setTitle("Pokemon Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMoves(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MovesTab.fxml"));
            Parent trainerRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene trainerScene = new Scene(trainerRoot);
            stage.setScene(trainerScene);
            stage.setTitle("Moves Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleItems(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ItemsTab.fxml"));
            Parent trainerRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene trainerScene = new Scene(trainerRoot);
            stage.setScene(trainerScene);
            stage.setTitle("Items Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTrainers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrainerTab.fxml"));
            Parent trainerRoot = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene trainerScene = new Scene(trainerRoot);
            stage.setScene(trainerScene);
            stage.setTitle("Trainer Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
