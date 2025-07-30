package com.pokedex.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

import com.pokedex.app.ItemBasic;

public class ViewItemsController {

    @FXML private TableView<ItemBasic> tableView;
    @FXML private TableColumn<ItemBasic, String> colName;
    @FXML private TableColumn<ItemBasic, String> colCategory;
    @FXML private TableColumn<ItemBasic, String> colDescription;
    @FXML private TableColumn<ItemBasic, String> colEffect;
    @FXML private TableColumn<ItemBasic, Integer> colBuyPrice;
    @FXML private TableColumn<ItemBasic, Integer> colSellPrice;

    private final ObservableList<ItemBasic> itemBasicList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEffect.setCellValueFactory(new PropertyValueFactory<>("effect"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        enableTextWrap(colDescription);
        enableTextWrap(colEffect);

        populateItems();
        tableView.setItems(itemBasicList);
    }

    private void enableTextWrap(TableColumn<ItemBasic, String> column) {
        column.setCellFactory(tc -> {
            TableCell<ItemBasic, String> cell = new TableCell<>() {
                private final javafx.scene.text.Text text = new javafx.scene.text.Text();
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty || item == null ? "" : item);
                }
            };
            return cell;
        });
    }

    private void populateItems() {
        itemBasicList.add(new ItemBasic("HP Up", "Vitamin", "A nutritious drink for Pokémon.", "+10 HP EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Protein", "Vitamin", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Iron", "Vitamin", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Carbos", "Vitamin", "A nutritious drink for Pokémon.", "+10 Speed EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Zinc", "Vitamin", "A nutritious drink for Pokémon.", "+10 Special Defense EVs", 10000, 5000));
        itemBasicList.add(new ItemBasic("Rare Candy", "Leveling Item", "A candy packed with energy.", "Increases level by 1", 4800, 2400));
        itemBasicList.add(new ItemBasic("Health Feather", "Feather", "A feather that slightly increases HP.", "+1 HP EV", 300, 150));
        itemBasicList.add(new ItemBasic("Muscle Feather", "Feather", "A feather that slightly increases Attack.", "+1 Attack EV", 300, 150));
        itemBasicList.add(new ItemBasic("Resist Feather", "Feather", "A feather that slightly increases Defense.", "+1 Defense EV", 300, 150));
        itemBasicList.add(new ItemBasic("Swift Feather", "Feather", "A feather that slightly increases Speed.", "+1 Speed EV", 300, 150));

        itemBasicList.add(new ItemBasic("Fire Stone", "Evolution Stone", "A stone that radiates heat.", "Evolves Vulpix, Growlithe, Eevee (Flareon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Water Stone", "Evolution Stone", "Blue, watery appearance.", "Evolves Poliwhirl, Shellder, Eevee (Vaporeon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Thunder Stone", "Evolution Stone", "Sparkles with electricity.", "Evolves Pikachu, Eevee (Jolteon), etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Leaf Stone", "Evolution Stone", "Leaf pattern.", "Evolves Gloom, Weepinbell, Exeggcute, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Moon Stone", "Evolution Stone", "Glows faintly.", "Evolves Clefairy, Jigglypuff, Nidorina, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Sun Stone", "Evolution Stone", "Glows like the sun.", "Evolves Gloom, Sunkern, Cottonee, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Shiny Stone", "Evolution Stone", "Sparkles brightly.", "Evolves Togetic, Roselia, Minccino, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Dusk Stone", "Evolution Stone", "Ominous appearance.", "Evolves Murkrow, Misdreavus, Doublade, etc.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Dawn Stone", "Evolution Stone", "Sparkles like morning sky.", "Evolves male Kirlia, female Snorunt.", 3000, 1500));
        itemBasicList.add(new ItemBasic("Ice Stone", "Evolution Stone", "Cold to the touch.", "Evolves Alolan Vulpix, Galarian Darumaka, etc.", 3000, 1500));
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
