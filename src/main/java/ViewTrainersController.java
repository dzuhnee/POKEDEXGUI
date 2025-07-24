package com.pokedex.app;

import com.pokedex.app.TrainerBasic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ViewTrainersController {
    @FXML private TableView<com.pokedex.app.TrainerBasic> trainersTable;

    @FXML private TableColumn<com.pokedex.app.TrainerBasic, Integer> idColumn;
    @FXML private TableColumn<com.pokedex.app.TrainerBasic, String> nameColumn;
    @FXML private TableColumn<com.pokedex.app.TrainerBasic, LocalDate> birthdateColumn;
    @FXML private TableColumn<com.pokedex.app.TrainerBasic, String> genderColumn;
    @FXML private TableColumn<com.pokedex.app.TrainerBasic, String> hometownColumn;
    @FXML private TableColumn<com.pokedex.app.TrainerBasic, String> descriptionColumn;

    @FXML private Button backButton;


    private ObservableList<com.pokedex.app.TrainerBasic> trainerData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("trainerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        hometownColumn.setCellValueFactory(new PropertyValueFactory<>("hometown"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadTrainerDataFromFile("trainers.txt");

        trainersTable.setItems(trainerData);
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        System.out.println("Back button clicked!"); // For debugging

        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/TrainerTab.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // add a label ??
            System.err.println("Error loading MainMenu.fxml: " + e.getMessage());
        }
    }

    private void loadTrainerDataFromFile(String filename) {
        trainerData.clear();

        String[] fieldOrder = {"ID", "Name", "Birthdate", "Gender", "Hometown", "Description"};
        String[] currentTrainerData = new String[fieldOrder.length];
        int fieldIndex = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("--------------------------------------------------") || line.equals("Trainer Info:")) {
                    if (fieldIndex == fieldOrder.length) {
                        int id = Integer.parseInt(currentTrainerData[0]);
                        LocalDate birthdate = LocalDate.parse(currentTrainerData[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        trainerData.add(new com.pokedex.app.TrainerBasic(
                                id, // Use the int id
                                currentTrainerData[1], // Name (still String)
                                birthdate, // Use the LocalDate birthdate
                                currentTrainerData[3], // Gender (still String)
                                currentTrainerData[4], // Hometown (still String)
                                currentTrainerData[5]  // Description (still String)
                        ));
                    }
                    currentTrainerData = new String[fieldOrder.length];
                    fieldIndex = 0;
                    continue; // is using continue okay?
                }

                for (int i = 0; i < fieldOrder.length; i++) {
                    String prefix = fieldOrder[i] + ": ";
                    if (line.startsWith(prefix)) {
                        currentTrainerData[i] = line.substring(prefix.length()).trim();
                        fieldIndex++;
                        break;
                    }
                }
            }

            if (fieldIndex == fieldOrder.length) {
                int id = Integer.parseInt(currentTrainerData[0]);
                LocalDate birthdate = LocalDate.parse(currentTrainerData[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                trainerData.add(new com.pokedex.app.TrainerBasic(
                        id,
                        currentTrainerData[1],
                        birthdate,
                        currentTrainerData[3],
                        currentTrainerData[4],
                        currentTrainerData[5]
                ));
            }
        } catch (IOException e) {
            System.err.println("Error reading trainer data from " + filename + ": " + e.getMessage());
        }
    }
}