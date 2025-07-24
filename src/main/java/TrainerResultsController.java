package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.pokedex.app.TrainerBasic;

public class TrainerResultsController {

    @FXML
    private Label searchLabel;

    @FXML
    private TableView<TrainerBasic> resultTable;

    @FXML
    private TableColumn<TrainerBasic, Integer> idColumn;

    @FXML
    private TableColumn<TrainerBasic, String> nameColumn;

    @FXML
    private TableColumn<TrainerBasic, LocalDate> birthdateColumn;

    @FXML
    private TableColumn<TrainerBasic, String> genderColumn;

    @FXML
    private TableColumn<TrainerBasic, String> hometownColumn;

    @FXML
    private TableColumn<TrainerBasic, String> descriptionColumn;

    private final ObservableList<TrainerBasic> allTrainers = FXCollections.observableArrayList();

    private ObservableList<TrainerBasic> lastSearchResults = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("trainerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        hometownColumn.setCellValueFactory(new PropertyValueFactory<>("hometown"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadTrainersFromFile();

        resultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);     // To select a trainer to manage
    }

    private void loadTrainersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("trainers.txt"))) {
            String line;
            int id = 0;
            String name = null;
            LocalDate birthdate = null;
            String gender = null;
            String hometown = null;
            String description = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("ID: ")) {
                    id = Integer.parseInt(line.substring(4));
                } else if (line.startsWith("Name: ")) {
                    name = line.substring(6);
                } else if (line.startsWith("Birthdate: ")) {
                    String dateStr = line.substring(11);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    birthdate = LocalDate.parse(dateStr, formatter);
                } else if (line.startsWith("Gender: ")) {
                    gender = line.substring(8);
                } else if (line.startsWith("Hometown: ")) {
                    hometown = line.substring(10);
                } else if (line.startsWith("Description: ")) {
                    description = line.substring(13);
                    if (id != 0 && name != null && birthdate != null && gender != null && hometown != null && description != null) {
                        allTrainers.add(new TrainerBasic(id, name, birthdate, gender, hometown, description));
                    }
                    // Reset
                    id = 0;
                    name = null;
                    birthdate = null;
                    gender = null;
                    hometown = null;
                    description = null;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading trainer data: " + e.getMessage());
            // Delete or nah?
            allTrainers.addAll(
                    new TrainerBasic(1, "Ash Ketchum", LocalDate.of(1987, 5, 22), "Male", "Pallet Town", "Pokemon Trainer from Kanto"),
                    new TrainerBasic(2, "Misty", LocalDate.of(1988, 3, 18), "Female", "Cerulean City", "Cerulean Gym Leader"),
                    new TrainerBasic(3, "Brock", LocalDate.of(1985, 9, 15), "Male", "Pewter City", "Former Pewter Gym Leader"),
                    new TrainerBasic(4, "Gary Oak", LocalDate.of(1987, 11, 22), "Male", "Pallet Town", "Pokemon Researcher")
            );
        }
    }

    public void setResults(List<TrainerBasic> results) {
        allTrainers.setAll(results); // replace the list with the passed results
        resultTable.setItems(allTrainers); // show it in the table
    }

    public void performSearch(String keyword) {
        searchLabel.setText("Search results for: " + keyword);

        ObservableList<TrainerBasic> filteredList = FXCollections.observableArrayList();

        for (TrainerBasic trainer : allTrainers) {
            if (trainer.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    trainer.getGender().toLowerCase().contains(keyword.toLowerCase()) ||
                    trainer.getHometown().toLowerCase().contains(keyword.toLowerCase()) ||
                    trainer.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(trainer);
            }
        }

        resultTable.setItems(filteredList);
        lastSearchResults.setAll(filteredList);

        if (filteredList.isEmpty()) {
            searchLabel.setText("No trainers found for: " + keyword);
        }
    }

    @FXML
    public void handleManage(ActionEvent event) {
        TrainerBasic selectedTrainer = resultTable.getSelectionModel().getSelectedItem();

        if (selectedTrainer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a trainer to manage.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTrainer.fxml"));
            Parent root = loader.load();

            // to pass the selected trainer BUT WHY ARE THEY IN REEEEEDD
            ManageTrainerController manageController = loader.getController();
            manageController.setTrainer(selectedTrainer);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleNewSearch(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SearchTrainer.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrainerResults.fxml"));
            Parent root = loader.load();

            TrainerResultsController controller = loader.getController();

            controller.setResults(lastSearchResults);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}