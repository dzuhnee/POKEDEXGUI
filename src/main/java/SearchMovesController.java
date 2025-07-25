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
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class SearchMovesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<com.pokedex.app.MoveBasic> resultTable;

    @FXML
    private TableColumn<com.pokedex.app.MoveBasic, String> colName;

    @FXML
    private TableColumn<com.pokedex.app.MoveBasic, String> colDescription;

    @FXML
    private TableColumn<com.pokedex.app.MoveBasic, String> colClassification;

    private ObservableList<MoveBasic> allMoveBasics = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colClassification.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClassification()));

        allMoveBasics.addAll(loadMovesFromFile());
    }

    public List<com.pokedex.app.MoveBasic> loadMovesFromFile() {
        List<com.pokedex.app.MoveBasic> moveBasics = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    String classification = parts[4].trim(); // use the last part
                    moveBasics.add(new com.pokedex.app.MoveBasic(name, description, classification));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moveBasics;
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            resultTable.setItems(FXCollections.observableArrayList());
            resultTable.setVisible(false);
            return;
        }

        List<com.pokedex.app.MoveBasic> filtered = allMoveBasics.stream()
                .filter(moveBasic -> moveBasic.getName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        resultTable.setItems(FXCollections.observableArrayList(filtered));
        resultTable.setVisible(true);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MovesTab.fxml")); // adjust path if needed
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
