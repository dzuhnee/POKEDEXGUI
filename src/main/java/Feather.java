package com.pokedex.app;

public class Feather extends Item {
    private String statAffected; // e.g., "HP", "Attack", "Defense", "Speed"

    public Feather(String name, String description, String effect,
                   int buyingPrice, int sellingPrice, int stock, String statAffected) {
        super(name, "Feather", description, effect, buyingPrice, sellingPrice, stock);
        this.statAffected = statAffected;
    }

    public String getStatAffected() {
        return statAffected;
    }

    public void use(Pokemon pokemon, PokemonManager manager) {
        int amount = 1; // Feathers typically increase EVs by 1
        pokemon.increaseStat(statAffected, amount);
        System.out.println(pokemon.getName() + "'s " + statAffected + " EVs increased by " + amount + " through " + getName() + "!");
        // Decrease stock logic here
    }
}