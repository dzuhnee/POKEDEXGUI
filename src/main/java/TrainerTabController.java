package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class TrainerTabController {

    @FXML
    private Button addBtn, viewBtn, searchBtn, backBtn;

    @FXML
    private void initialize() {
        addBtn.setOnAction(e -> openAddTrainer());
        viewBtn.setOnAction(e -> openViewTrainer());
        searchBtn.setOnAction(e -> openSearchTrainer());
    }

    private void openAddTrainer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTrainer.fxml"));
            Parent addTrainerRoot = loader.load();
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.setScene(new Scene(addTrainerRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openViewTrainer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewTrainers.fxml"));
            Parent viewTrainerRoot = loader.load();
            Stage stage = (Stage) viewBtn.getScene().getWindow();
            stage.setScene(new Scene(viewTrainerRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSearchTrainer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SearchTrainer.fxml"));
            Parent searchTrainerRoot = loader.load();
            Stage stage = (Stage) viewBtn.getScene().getWindow();
            stage.setScene(new Scene(searchTrainerRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
