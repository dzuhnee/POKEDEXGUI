package com.pokedex.app;

public class Vitamin extends Item {
    private String statAffected; // e.g., "HP", "Attack", "Defense", "Speed", "Special Attack", "Special Defense"

    public Vitamin(String name, String description, String effect,
                   int buyingPrice, int sellingPrice, int stock, String statAffected) {
        super(name, "Vitamin", description, effect, buyingPrice, sellingPrice, stock);
        this.statAffected = statAffected;
    }

    public String getStatAffected() {
        return statAffected;
    }

    public void use(Pokemon pokemon, PokemonManager manager) {
        int amount = 10; // Vitamins typically increase EVs by a set amount (e.g., 10)

        pokemon.increaseStat(statAffected, amount);

        System.out.println(pokemon.getName() + "'s " + statAffected + " increased by " + amount + " through " + getName() + "!");
        // Optional: Decrease the item's stock after use
        // setStock(getStock() - 1);
    }
}
