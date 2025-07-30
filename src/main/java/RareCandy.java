package com.pokedex.app;

import com.pokedex.app.Item;
import com.pokedex.app.Pokemon;
import com.pokedex.app.PokemonManager;

public class RareCandy extends Item {

    public RareCandy() {
        super("Rare Candy", "Level-Up Item",
                "A candy packed with energy.", "Increases level by 1",
                4800, 2400, 10);
    }

    public RareCandy(String name, String category, String description, String effect,
                     int buyingPrice, int sellingPrice, int stock) {
        super(name, category, description, effect, buyingPrice, sellingPrice, stock);
    }

    @Override
    public String use(Pokemon pokemon, PokemonManager manager) {
        String originalName = pokemon.getName(); // Save before evolution
        int originalLevel = pokemon.getBaseLevel();

        boolean evolved = pokemon.levelUpWithRareCandy(manager);

        StringBuilder result = new StringBuilder();
        result.append(originalName)
                .append(" leveled up from Level ")
                .append(originalLevel)
                .append(" to Level ")
                .append(pokemon.getBaseLevel())
                .append("!");

        if (evolved && !originalName.equals(pokemon.getName())) {
            result.append(" ").append(originalName)
                    .append(" evolved into ").append(pokemon.getName()).append("!");
        }

        return result.toString();
    }


    @Override
    public String getPreviewEffect(Pokemon pokemon, PokemonManager manager) {
        int currentLevel = pokemon.getBaseLevel(); // current level
        int previewLevel = currentLevel + 1;

        int evolvesToDex = pokemon.getEvolvesTo();
        int evolutionLevel = pokemon.getEvolutionLevel();

        StringBuilder sb = new StringBuilder();
        sb.append("Effect: ").append(getEffect()).append("\n");
        sb.append(pokemon.getName())
                .append(" will become level ")
                .append(previewLevel)
                .append(".\n");

        if (evolvesToDex != 0 && previewLevel >= evolutionLevel) {
            Pokemon evolved = manager.getPokemonByDex(evolvesToDex);
            if (evolved != null) {
                sb.append(pokemon.getName())
                        .append(" will evolve into ")
                        .append(evolved.getName())
                        .append("!");
            }
        }

        return sb.toString();
    }
}

// TEST TEST
