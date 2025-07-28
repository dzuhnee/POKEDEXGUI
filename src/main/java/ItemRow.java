// File: ItemRow.java
package com.pokedex.app;

import com.pokedex.app.Item;

public class ItemRow {
    private final Item item;
    private final int quantity;

    public ItemRow(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() { return item; }
    public String getName() { return item.getName(); }
    public String getDescription() { return item.getDescription(); }
    public int getSellingPrice() { return item.getSellingPrice(); }
    public int getQuantity() { return quantity; }
}
