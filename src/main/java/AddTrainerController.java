package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddTrainerController {

    @FXML
    private TextField trainerIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField birthdateField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField hometownField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Label feedbackLabel;

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrainerTab.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) trainerIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSave() {
        String trainerId = trainerIdField.getText().trim();
        String name = nameField.getText().trim();
        String birthdate = birthdateField.getText().trim();
        String gender = genderField.getText().trim();
        String hometown = hometownField.getText().trim();
        String description = descriptionField.getText().trim();

        // Ensure all fields contain user input
        if (trainerId.isEmpty() || name.isEmpty() || birthdate.isEmpty() || gender.isEmpty() || hometown.isEmpty() || description.isEmpty()) {
            feedbackLabel.setText("Please fill in all required fields!");
            return;
        }

        // Check if trainerId is a valid number
        try {
            Integer.parseInt(trainerId);
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Trainer ID must be a number!");
            return;
        }

        // Check if birthdate is valid
        LocalDate parsedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            parsedDate = LocalDate.parse(birthdate, formatter);

            if (parsedDate.isAfter(LocalDate.now())) {
                feedbackLabel.setText("Birthdate can't be in the future!");
                return;
            }
        } catch (DateTimeParseException e) {
            feedbackLabel.setText("Birthdate must be in the format yyyy-MM-dd!");
            return;
        }

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trainers.txt", true))) {
            writer.write("Trainer Info:");
            writer.newLine();
            writer.write("ID: " + trainerId);
            writer.newLine();
            writer.write("Name: " + name);
            writer.newLine();
            writer.write("Birthdate: " + birthdate);
            writer.newLine();
            writer.write("Gender: " + gender);
            writer.newLine();
            writer.write("Hometown: " + hometown);
            writer.newLine();
            writer.write("Description: " + description);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving trainer data: " + e.getMessage());
        }

        trainerIdField.clear();
        nameField.clear();
        birthdateField.clear();
        genderField.clear();
        hometownField.clear();
        descriptionField.clear();
        feedbackLabel.setText("Trainer info saved successfully!");
    }
}
