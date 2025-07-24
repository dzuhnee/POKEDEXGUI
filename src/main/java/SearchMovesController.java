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
import java.util.List;


import com.pokedex.app.Move;

public class SearchMovesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Move> resultTable;

    @FXML
    private TableColumn<Move, String> colName;

    @FXML
    private TableColumn<Move, String> colDescription;

    @FXML
    private TableColumn<Move, String> colClassification;

    private ObservableList<Move> allMoves = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colClassification.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClassification()));

        allMoves.addAll(loadMovesFromFile());
    }

    public List<Move> loadMovesFromFile() {
        List<Move> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("moves.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    String classification = parts[4].trim(); // use the last part
                    moves.add(new Move(name, description, classification));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moves;
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            resultTable.setItems(FXCollections.observableArrayList());
            resultTable.setVisible(false);
            return;
        }

        List<Move> filtered = allMoves.stream()
                .filter(move -> move.getName().toLowerCase().contains(keyword))
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
