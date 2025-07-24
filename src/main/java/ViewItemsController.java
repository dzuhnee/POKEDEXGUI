package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;


import com.pokedex.app.Item;

public class ViewItemsController {

    @FXML private TableView<Item> tableView;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colCategory;
    @FXML private TableColumn<Item, String> colDescription;
    @FXML private TableColumn<Item, String> colEffect;
    @FXML private TableColumn<Item, Integer> colBuyPrice;
    @FXML private TableColumn<Item, Integer> colSellPrice;

    private final ObservableList<Item> itemList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEffect.setCellValueFactory(new PropertyValueFactory<>("effect"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        populateItems();
        tableView.setItems(itemList);
    }

    private void populateItems() {
        itemList.add(new Item("HP Up", "Vitamin", "A nutritious drink for Pokémon.", "+10 HP EVs", 10000, 5000));
        itemList.add(new Item("Protein", "Vitamin", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000));
        itemList.add(new Item("Iron", "Vitamin", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000));
        itemList.add(new Item("Carbos", "Vitamin", "A nutritious drink for Pokémon.", "+10 Speed EVs", 10000, 5000));
        itemList.add(new Item("Zinc", "Vitamin", "A nutritious drink for Pokémon.", "+10 Special Defense EVs", 10000, 5000));
        itemList.add(new Item("Rare Candy", "Leveling Item", "A candy packed with energy.", "Increases level by 1", -1, 2400));
        itemList.add(new Item("Health Feather", "Feather", "A feather that slightly increases HP.", "+1 HP EV", 300, 150));
        itemList.add(new Item("Muscle Feather", "Feather", "A feather that slightly increases Attack.", "+1 Attack EV", 300, 150));
        itemList.add(new Item("Resist Feather", "Feather", "A feather that slightly increases Defense.", "+1 Defense EV", 300, 150));
        itemList.add(new Item("Swift Feather", "Feather", "A feather that slightly increases Speed.", "+1 Speed EV", 300, 150));

        itemList.add(new Item("Fire Stone", "Evolution Stone", "A stone that radiates heat.", "Evolves Vulpix, Growlithe, Eevee (Flareon), etc.", 3000, 1500));
        itemList.add(new Item("Water Stone", "Evolution Stone", "Blue, watery appearance.", "Evolves Poliwhirl, Shellder, Eevee (Vaporeon), etc.", 3000, 1500));
        itemList.add(new Item("Thunder Stone", "Evolution Stone", "Sparkles with electricity.", "Evolves Pikachu, Eevee (Jolteon), etc.", 3000, 1500));
        itemList.add(new Item("Leaf Stone", "Evolution Stone", "Leaf pattern.", "Evolves Gloom, Weepinbell, Exeggcute, etc.", 3000, 1500));
        itemList.add(new Item("Moon Stone", "Evolution Stone", "Glows faintly.", "Evolves Clefairy, Jigglypuff, Nidorina, etc.", -1, 1500));
        itemList.add(new Item("Sun Stone", "Evolution Stone", "Glows like the sun.", "Evolves Gloom, Sunkern, Cottonee, etc.", 3000, 1500));
        itemList.add(new Item("Shiny Stone", "Evolution Stone", "Sparkles brightly.", "Evolves Togetic, Roselia, Minccino, etc.", 3000, 1500));
        itemList.add(new Item("Dusk Stone", "Evolution Stone", "Ominous appearance.", "Evolves Murkrow, Misdreavus, Doublade, etc.", 3000, 1500));
        itemList.add(new Item("Dawn Stone", "Evolution Stone", "Sparkles like morning sky.", "Evolves male Kirlia, female Snorunt.", 3000, 1500));
        itemList.add(new Item("Ice Stone", "Evolution Stone", "Cold to the touch.", "Evolves Alolan Vulpix, Galarian Darumaka, etc.", 3000, 1500));
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ItemsTab.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
