package com.pokedex.app;

public class RareCandy extends Item {
    public RareCandy(String name, String description, String effect,
                     int buyingPrice, int sellingPrice, int stock) {
        // MODIFIED: Calling super with all 7 arguments, including the proper int values
        super(name, "Leveling Item", description, effect, buyingPrice, sellingPrice, stock);
    }

    public void use(Pokemon pokemon, PokemonManager manager) {
        pokemon.levelUpWithRareCandy(manager); // Correctly passes the manager
        System.out.println(pokemon.getName() + " leveled up to Level " + pokemon.getBaseLevel() + " with " + getName() + "!");
    }
}
