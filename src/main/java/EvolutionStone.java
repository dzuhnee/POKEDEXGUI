package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.PokemonManager;

// C:\Users\Kyle Dominique\Desktop\gui\PokedexTest\src\main\java\EvolutionStone.java:6:8
//java: com.pokedex.app.EvolutionStone is not abstract and does not override abstract method use(com.pokedex.app.Pokemon,com.pokedex.app.PokemonManager) in com.pokedex.app.Item

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
