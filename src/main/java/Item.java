package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single Pokémon item with attributes such as name, category,
 * description, effect, buying/selling price, and stock quantity.
 */
// delete Items.java later
public abstract class Item {
    // These are the properties of the item
    private final int buyingPrice;
    private final String name;
    private final String category;
    private final String description;
    private final String effect;
    private final int sellingPrice;
    private int stock;

    /**
     * Constructs an Item with all necessary properties.
     *
     * @param name         the name of the item
     * @param category     the category of the item (e.g., Vitamin, Feather)
     * @param description  a short description of the item
     * @param effect       the effect it has on Pokémon
     * @param buyingPrice  price to buy the item
     * @param sellingPrice price to sell the item
     * @param stock        initial stock quantity
     */
    public Item(String name, String category, String description, String effect,
                int buyingPrice, int sellingPrice, int stock) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.effect = effect;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
    }

    // Getters

    /**
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * @return the category of the item
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the effect the item has
     */
    public String getEffect() {
        return effect;
    }

    /**
     * @return the buying price of the item
     */
    public int getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * @return the selling price of the item
     */
    public int getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @return the current stock quantity
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock value for the item.
     *
     * @param stock the new stock quantity
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    public abstract void use(Pokemon pokemon, PokemonManager manager);

    // Might remove ??
    public String displayInfo() {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(name).append("\n");
        details.append("Category: ").append(category).append("\n");
        details.append("Description: ").append(description).append("\n");
        details.append("Effect: ").append(effect).append("\n");
        details.append("Buying Price: ");
        if (buyingPrice == -1) {
            details.append("N/A (Not for sale)\n");
        } else {
            details.append("$").append(buyingPrice).append("\n");
        }
        details.append("Selling Price: ");
        if (sellingPrice == -1) {
            details.append("N/A (Cannot be sold)\n");
        } else {
            details.append("$").append(sellingPrice).append("\n");
        }
        details.append("Current Stock: ").append(stock);
        return details.toString();
    }
}
