package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Item;
import com.pokedex.app.Vitamin;
import com.pokedex.app.RareCandy;
import com.pokedex.app.Feather;
import com.pokedex.app.EvolutionStone;

/**
 * Manages a collection of Pokémon items and provides functionality
 * for viewing, searching, and retrieving items.
 */
class ItemManager {

    private final List<Item> itemList; // List to store all the item objects


    public ItemManager() {
        itemList = new ArrayList<>();
        populateInitialItems();
    }

    /**
     * Populates the item list with predefined sample items.
     * Includes Vitamins, Feathers, Leveling Items, and Evolution Stones.
     */
    public void populateInitialItems() {
        // Vitamins (boost EVs)
        itemList.add(new Vitamin("Calcium", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000, 10, "Attack"));
        itemList.add(new Vitamin("Carbos", "A nutritious drink for Pokémon.", "+10 Speed EVs", 10000, 5000, 10, "Speed"));
        itemList.add(new Vitamin("HP Up", "A nutritious drink for Pokémon.", "+10 HP EVs", 10000, 5000, 10, "HP"));
        itemList.add(new Vitamin("Iron", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000, 10, "Defense"));
        itemList.add(new Vitamin("Protein", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000, 10, "Attack"));
        itemList.add(new Vitamin("Zinc", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000, 10, "Defense"));

        // Feathers
        itemList.add(new Feather("Health Feather", "Slightly increases HP.", "+1 HP EV", 300, 150, 10, "HP"));
        itemList.add(new Feather("Muscle Feather", "Slightly increases Attack.", "+1 Attack EV", 300, 150, 10, "Attack"));
        itemList.add(new Feather("Resist Feather", "Slightly increases Defense.", "+1 Defense EV", 300, 150, 10, "Defense"));
        itemList.add(new Feather("Swift Feather", "Slightly increases Speed.", "+1 Speed EV", 300, 150, 10, "Speed"));
        itemList.add(new Feather("Genius Feather", "Slightly increases Attack.", "+1 Attack EV", 300, 150, 10, "Attack"));
        itemList.add(new Feather("Clever Feather", "Slightly increases Defense.", "+1 Defense EV", 300, 150, 10, "Defense"));

        // Others
        itemList.add(new RareCandy("Rare Candy", "Level-Up Item", "A candy packed with energy.", "Increases level by 1", 500, -1, 0));  // -1 or 0 indicates it cannot be sold

        // Evolution Stones
        itemList.add(new EvolutionStone("Fire Stone", "Radiates heat.", "Evolves Vulpix, Growlithe, Eevee, etc.", 3000, 1500, 10, "Fire"));
        itemList.add(new EvolutionStone("Water Stone", "Blue, watery appearance.", "Evolves Poliwhirl, Shellder, Eevee, etc.", 3000, 1500, 10, "Water"));
        itemList.add(new EvolutionStone("Thunder Stone", "Sparkles with electricity.", "Evolves Pikachu, Eelektrik, Eevee, etc.", 3000, 1500, 10, "Electric"));
        itemList.add(new EvolutionStone("Leaf Stone", "Leaf pattern.", "Evolves Gloom, Weepinbell, Exeggcute etc.", 3000, 1500, 10, "Grass"));
        itemList.add(new EvolutionStone("Moon Stone", "Glows faintly.", "Evolves Nidorina, Clefairy, Jigglypuff, etc.", -1, 1500, 10, "Moon"));
        itemList.add(new EvolutionStone("Sun Stone", "Glows like the sun.", "Evolves Gloom, Sunkern, Cottonee, etc.", 3000, 1500, 10, "Sun"));
        itemList.add(new EvolutionStone("Shiny Stone", "Sparkles brightly.", "Evolves Togetic, Roselia, Minccino, etc.", 3000, 1500, 10, "Fairy"));
        itemList.add(new EvolutionStone("Dusk Stone", "Ominous appearance.", "Evolves Murkrow, Misdreavus, Doublade, etc.", 3000, 1500, 10, "Dark"));
        itemList.add(new EvolutionStone("Dawn Stone", "Sparkles like the morning sky.", "Evolves male Kirlia and female Snorunt.", 3000, 1500, 10, "Psychic"));
        itemList.add(new EvolutionStone("Ice Stone", "Cold to the touch.", "Evolves Alolan Vulpix, etc.", 3000, 1500, 10, "Ice"));
    }


    public List<Item> getAllItems() {
        return new ArrayList<>(itemList);
    }

    /**
     * Searches items by their name or effect using a keyword.
     *
     * @param keyword the search keyword entered by the user
     */
    public List<Item> searchItemsByNameOrEffect(String keyword) {
        List<Item> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Item item : itemList) {
            if (item.getName().toLowerCase().contains(lowerKeyword) ||
                    item.getEffect().toLowerCase().contains(lowerKeyword)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Searches items by category (e.g., Vitamin, Feather).
     *
     * @param category the category name to search
     */
    public List<Item> searchByCategory(String category) {
        List<Item> results = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Finds and returns an item by its name (case-insensitive).
     *
     * @param heldItem the name of the item to find
     * @return the Item if found, or null otherwise
     */
    public Item findItem(String heldItem) {
        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(heldItem)) {
                return item;
            }
        }
        return null;
    }

    public boolean itemExists(String item) {
        return findItem(item) != null;
    }

    public int buyItem(String itemName, int quantity) {
        Item item = findItem(itemName);

        if (item == null) {      // Item not found
            return -1;
        }

        if (quantity <= 0) {        // Invalid quantity
            return -1;
        }

        if (item.getBuyingPrice() == -1) {      // There's no negative price
            return -1;
        }

        if (item.getStock() >= quantity) {
            item.setStock(item.getStock() - quantity);
            return item.getBuyingPrice() * quantity;
        } else {
            return -1;
        }
    }

    public int sellItem(String itemName, int quantity) {
        Item item = findItem(itemName);

        if (item == null) {      // Item not found
            return -1;
        }

        if (quantity <= 0) {        // Invalid quantity
            return -1;
        }

        if (item.getSellingPrice() == -1) {      // There's no negative na price
            return -1;
        }

        item.setStock(item.getStock() + quantity);
        return item.getSellingPrice() * quantity;
    }


}