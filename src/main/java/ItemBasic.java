package com.pokedex.app;

public class ItemBasic {
    private String name;
    private String category;
    private String description;
    private String effect;
    private int buyingPrice;
    private int sellingPrice;

    public ItemBasic(String name, String category, String description, String effect, int buyingPrice, int sellingPrice) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.effect = effect;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getEffect() {
        return effect;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }
}
