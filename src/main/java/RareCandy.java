package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.Pokemon;
import com.pokedex.app.PokemonManager;

public class RareCandy extends Item {

    public RareCandy(String name, String description, String effect,
                     int buyingPrice, int sellingPrice, int stock) {
        super(name, "Rare Candy", description, effect, buyingPrice, sellingPrice, stock);
    }

    @Override
    public String use(Pokemon pokemon, PokemonManager manager) {
        boolean evolved = pokemon.levelUpWithRareCandy(manager);

        String message = pokemon.getName() + " leveled up to " + pokemon.getBaseLevel() + "!";

        if (evolved) {
            message += "\nIt evolved into " + pokemon.getName() + "!";
        }

        return message;
    }
}
