package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.Pokemon;
import com.pokedex.app.PokemonManager;

public class EvolutionStone extends Item {
    private final String type;

    public EvolutionStone(String name, String description, String effect,
                          int buyingPrice, int sellingPrice, int stock, String type) {
        super(name, "Evolution Stone", description, effect, buyingPrice, sellingPrice, stock);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String use(Pokemon pokemon, PokemonManager manager) {
        boolean evolved = pokemon.evolveUsingStone(type, manager);

        if (evolved) {
            return pokemon.getName() + " evolved successfully using the " + getName() + "!";
        } else {
            return "The " + getName() + " had no effect on " + pokemon.getName() + ".";
        }
    }
}
