package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.stage.Stage;

public class SearchItemsController implements Initializable {

    @FXML private TableView<ItemBasic> tableView;
    @FXML private TableColumn<ItemBasic, String> colName;
    @FXML private TableColumn<ItemBasic, String> colCategory;
    @FXML private TableColumn<ItemBasic, String> colDescription;
    @FXML private TableColumn<ItemBasic, String> colEffect;
    @FXML private TableColumn<ItemBasic, Integer> colBuyPrice;
    @FXML private TableColumn<ItemBasic, Integer> colSellPrice;
    @FXML private TextField searchField;

    private ObservableList<ItemBasic> itemBasicList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateItems();

        FilteredList<ItemBasic> filteredData = new FilteredList<>(itemBasicList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(itemBasic -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return itemBasic.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colCategory.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        colDescription.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        colEffect.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEffect()));
        colBuyPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getBuyingPrice()).asObject());
        colSellPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSellingPrice()).asObject());

        tableView.setItems(filteredData);
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().toLowerCase();

        ObservableList<ItemBasic> filtered = FXCollections.observableArrayList();
        for (ItemBasic itemBasic : itemBasicList) {  // <- use itemList instead of allItems
            if (itemBasic.getName().toLowerCase().contains(keyword) ||
                    itemBasic.getCategory().toLowerCase().contains(keyword) ||
                    itemBasic.getEffect().toLowerCase().contains(keyword)) {
                filtered.add(itemBasic);
            }
        }

        tableView.setItems(filtered);
        tableView.setVisible(true);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ItemsTab.fxml")); 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void populateItems() {
        itemBasicList.add(new ItemBasic("HP Up", "Vitamin", "A nutritious drink for Pokémon.", "+10 HP EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Protein", "Vitamin", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Iron", "Vitamin", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Carbos", "Vitamin", "A nutritious drink for Pokémon.", "+10 Speed EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Zinc", "Vitamin", "A nutritious drink for Pokémon.", "+10 Special Defense EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Rare Candy", "Leveling Item", "A candy packed with energy.", "Increases level by 1", -1, 2400));
        itemBasicList.add(new ItemBasic("Health Feather", "Feather", "A feather that slightly increases HP.", "+1 HP EV", 300, 150));
        itemBasicList.add(new ItemBasic("Muscle Feather", "Feather", "A feather that slightly increases Attack.", "+1 Attack EV", 300, 150));
        itemBasicList.add(new ItemBasic("Resist Feather", "Feather", "A feather that slightly increases Defense.", "+1 Defense EV", 300, 150));
        itemBasicList.add(new ItemBasic("Swift Feather", "Feather", "A feather that slightly increases Speed.", "+1 Speed EV", 300, 150));
        itemBasicList.add(new ItemBasic("Fire Stone", "Evolution Stone", "A stone that radiates heat.", "Evolves Vulpix, Growlithe, Eevee (Flareon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Water Stone", "Evolution Stone", "Blue, watery appearance.", "Evolves Poliwhirl, Shellder, Eevee (Vaporeon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Thunder Stone", "Evolution Stone", "Sparkles with electricity.", "Evolves Pikachu, Eevee (Jolteon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Leaf Stone", "Evolution Stone", "Leaf pattern.", "Evolves Gloom, Weepinbell, Exeggcute, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Moon Stone", "Evolution Stone", "Glows faintly.", "Evolves Clefairy, Jigglypuff, Nidorina, etc.", -1, 1500));
        itemBasicList.add(new ItemBasic("Sun Stone", "Evolution Stone", "Glows like the sun.", "Evolves Gloom, Sunkern, Cottonee, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Shiny Stone", "Evolution Stone", "Sparkles brightly.", "Evolves Togetic, Roselia, Minccino, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Dusk Stone", "Evolution Stone", "Ominous appearance.", "Evolves Murkrow, Misdreavus, Doublade, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Dawn Stone", "Evolution Stone", "Sparkles like morning sky.", "Evolves male Kirlia, female Snorunt.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Ice Stone", "Evolution Stone", "Cold to the touch.", "Evolves Alolan Vulpix, Galarian Darumaka, etc.", 3000, 1500));
    }
}
