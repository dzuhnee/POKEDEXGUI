package com.pokedex.app;

/**
 * Utility class for validating and listing valid Pokémon types.
 */
public class TypeUtils {
    /**
     * An array of valid Pokémon types. This list includes all recognized types
     * in the mainline Pokémon games.
     */
    private static final String[] VALID_TYPES = {
            "normal", "fire", "water", "electric", "grass", "ice",
            "fighting", "poison", "ground", "flying", "psychic", "bug",
            "rock", "ghost", "dragon", "dark", "steel", "fairy"
    };

    /**
     * Checks whether a given string is a valid Pokémon type.
     *
     * @param type the type to validate
     * @return {@code true} if the input matches a valid type (case-insensitive), {@code false} otherwise
     */
    public static boolean isValidType(String type) {
        for (String validType : VALID_TYPES) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        System.out.println(type + " is not a type!");
        return false;
    }
}
