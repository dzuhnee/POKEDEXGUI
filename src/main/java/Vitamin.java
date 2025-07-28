package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.Pokemon;
import com.pokedex.app.PokemonManager;

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

    @Override
    public String use(Pokemon pokemon, PokemonManager manager) {
        int amount = 10;

        pokemon.increaseStat(statAffected, amount);

        return pokemon.getName() + "'s " + statAffected + " increased by " + amount + " through " + getName() + "!";
    }
}
