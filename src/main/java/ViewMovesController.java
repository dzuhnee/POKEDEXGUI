package com.pokedex.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ViewMovesController {

    @FXML private TableView<MoveBasic> movesTable;
    @FXML private TableColumn<MoveBasic, String> nameColumn;
    @FXML private TableColumn<MoveBasic, String> descriptionColumn;
    @FXML private TableColumn<MoveBasic, String> classificationColumn;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        // Setup table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));

        List<MoveBasic> moveBasicList = new ArrayList<>();

        // Add default moves first
        moveBasicList.add(new MoveBasic("Tackle", "A basic physical attack", "TM (Technical Machine)"));
        moveBasicList.add(new MoveBasic("Defend", "Brace to reduce damage", "HM (Hidden Machine"));

        // Then try to load moves from file
        try {
            List<String> lines = Files.readAllLines(Paths.get("moves.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    String classification = parts[parts.length - 1].trim();

                    moveBasicList.add(new MoveBasic(name, description, classification));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load move.txt. Default moves will be used.");
        }

        movesTable.getItems().addAll(moveBasicList);
    }



    @FXML
    public void handleBackButton(ActionEvent event) {
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
