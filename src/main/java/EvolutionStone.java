package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.PokemonManager;

public class EvolutionStone extends Item {
    private String stoneType; // e.g., "Fire", "Water", "Thunder", "Leaf", "Moon", etc.

    public EvolutionStone(String name, String description, String effect,
                          int buyingPrice, int sellingPrice, int stock, String stoneType) {
        // MODIFIED: Calling super() with all 7 arguments, ensuring type consistency
        super(name, "Evolution Stone", description, effect, buyingPrice, sellingPrice, stock);
        this.stoneType = stoneType;
    }

    public String getStoneType() {
        return stoneType;
    }

    public void use(com.pokedex.app.Pokemon pokemon, PokemonManager manager) {
        boolean evolved = pokemon.evolveUsingStone(this.stoneType, manager); // Correctly passes the manager

        if (evolved) {
            System.out.println(pokemon.getName() + " evolved using " + getName() + "!");
        } else {
            System.out.println(pokemon.getName() + " cannot evolve with " + getName() + ".");
        }
    }
}
