package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class manages a list of Pokémon and allows adding, displaying, and
 * searching Pokémon.
 */
public class PokemonManager {
    private Scanner scan = new Scanner(System.in);
    private final List<Pokemon> pokemons = new ArrayList<>();

    public PokemonManager(final Scanner scanner) {
        this.scan = scanner;
        populateInitialPokemon();
    }

    /**
     * Prompts the user to enter all necessary data to add a new Pokémon.
     * Validates input formats and adds the Pokémon to the list once confirmed.
     *
     * @return true if the Pokémon was successfully added, false otherwise
     */
    public boolean addPokemon() {
        // Variables to store data temporarily
        int pokedexNumber;
        String name;
        String primaryType = null;
        String secondaryType = null;
        int hp, defense, attack, speed;
        int evolvesFrom, evolvesTo;
        int baseLevel, evolutionLevel;

//        System.out.printf("\n--- Add Pokémon ---\n\n");
//
//        // Pokédex Number
//        do {
//            pokedexNumber = readValidInt(scan, "Pokédex number (1-1010)");
//
//            if (!isValidDexNumber(pokedexNumber)) {
//                System.out.println("Pokedex already exists or invalid input. Please try again.");
//            }
//        } while (true);

        // Dummy values for compilation
        pokedexNumber = 9999;
        name = "NewPokemon";
        primaryType = "Normal";
        secondaryType = null;
        baseLevel = 1;
        evolvesFrom = 0;
        evolvesTo = 0;
        evolutionLevel = 0;
        hp = 50;
        attack = 50;
        defense = 50;
        speed = 50;

//        // Name - Pokémon name
//        name = readValidString(scan, "name", "[A-Za-z\\s]+");
//
//        // Type 1 - Pokémon's primary type
//        System.out.println("--------------------- CHOOSE TYPE FROM ---------------------------");
//        System.out.println("Normal      Fire       Water     Electric      Grass        Ice");
//        System.out.println("Fighting    Poison     Ground    Flying        Psychic      Bug");
//        System.out.println("Rock        Ghost      Dragon    Dark          Steel        Fairy");
//        System.out.println("------------------------------------------------------------------");
//        do {
//            primaryType = readValidString(scan, "primary type", "[A-Za-z\\s]+");
//        } while (!TypeUtils.isValidType(primaryType));
//
//        // Type 2 - Pokémon's secondary type
//        System.out.print("Does " + name + " has a secondary type? [Y/N]: ");
//        String choice;
//        do {
//            choice = scan.nextLine();
//            if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
//                System.out.print("Invalid input. Please try again: ");
//            }
//        } while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));
//        if (choice.equalsIgnoreCase("Y")) {
//            do {
//                secondaryType = readValidString(scan, "secondary type", "[A-Za-z\\s]+");
//            } while (!TypeUtils.isValidType(secondaryType));
//        }
//
//        // Base Level
//        baseLevel = readValidInt(scan, "base level");
//
//        // Evolves from - the Pokémon that the current Pokémon transformed from
//        do {
//            evolvesFrom = readValidInt(scan, "dex number that Pokémon evolves from");
//        } while (!isValidDexNumber(evolvesFrom));
//
//        // Evolves to - the Pokémon that the current Pokémon will transform into
//        do {
//            evolvesTo = readValidInt(scan, "dex number that Pokémon evolves to");
//        } while (!isValidDexNumber(evolvesTo));
//
//        // Evolution Level - specific level a Pokémon must reach to evolve into its next form
//        do {
//            evolutionLevel = readValidInt(scan, "evolution level");
//        } while (!isValidDexNumber(evolutionLevel));
//
//        // Base Stats
//        System.out.printf("\nEnter the Base Stats\n");
//        hp = PokemonBaseStats.readValidBaseStat(scan, "HP");
//        attack = PokemonBaseStats.readValidBaseStat(scan, "Attack");
//        defense = PokemonBaseStats.readValidBaseStat(scan, "Defense");
//        speed = PokemonBaseStats.readValidBaseStat(scan, "Speed");

        // Instantiate Pokémon
        Pokemon pokemon;
        if (secondaryType != null) {
            pokemon = new Pokemon(pokedexNumber, name, primaryType, secondaryType, baseLevel, evolvesFrom, evolvesTo,
                    evolutionLevel, hp, attack, defense, speed);
        } else {
            pokemon = new Pokemon(pokedexNumber, name, primaryType, baseLevel, evolvesFrom, evolvesTo, evolutionLevel,
                    hp, attack, defense, speed);
        }

//        // Confirmation
//        System.out.printf("\n" + name + " is ready to join! Add to your Pokémon [Y/N]: ");
//        String c;
//        do {
//            c = scan.nextLine();
//            if (!c.equalsIgnoreCase("Y") && !c.equalsIgnoreCase("N")) {
//                System.out.print("Invalid input. Please try again: ");
//            }
//        } while (!c.equalsIgnoreCase("Y") && !c.equalsIgnoreCase("N"));

        boolean confirmed = true;

        if (confirmed) {
            addPokemon(pokemon);
//            System.out.println("Pokémon \"" + name + "\" added successfully!");
//            System.out.println("");
            return true;
        }
        return false;
    }

    public boolean addPokemon(Pokemon pokemon) {
        if (getPokemonByDex(pokemon.getPokedexNumber()) != null) {
            return false;
        }
        pokemons.add(pokemon);
        return true;
    }

    /**
     * Displays all Pokémon currently stored in the system.
     */
    public void displayAllPokemon() {
//        System.out.printf("\n--- View All Pokémon ---\n\n");

        if (pokemons.isEmpty()) {
//            System.out.println("No Pokémon in the database.");
            return;
        }

//        divider();
//        header();
//        divider();
//
//        for (Pokemon p : pokemons) {
//            p.displayInfo();
//        }
//
//        System.out.println("");
    }

    public Pokemon getPokemonByDex(int pokedexNumber) {
        for (Pokemon p : pokemons) {
            if (p.getPokedexNumber() == pokedexNumber) {
                return p;
            }
        }
        return null;
    }

    public String getNameByDex(int pokedexNumber) {
        Pokemon p = getPokemonByDex(pokedexNumber);
        if (p != null) {
            return p.getName();
        }
        return null;
    }

    public List<String> getAllPokemonInfo() {
        List<String> output = new ArrayList<>();
        for (Pokemon p : pokemons) {
            output.add(p.displayInfo());
        }
        return output;
    }

    public List<Pokemon> getAllPokemon() {
        return new ArrayList<>(pokemons);
    }

    /**
     * Searches the Pokémon list for any Pokémon whose name contains the given
     * string.
     *
     * @param s the name or part of a name to search for
     */
    public List<Pokemon> searchByName(String s) {
        List<Pokemon> matches = new ArrayList<>();

        for (Pokemon p : pokemons) {
            if (p.getName().toLowerCase().contains(s.toLowerCase())) {
                matches.add(p);
            }
        }

        return matches;
    }

    /**
     * Searches the Pokémon list by type (either primary or secondary).
     *
     * @param s the type name to search for
     */
    public List<Pokemon> searchByType(String s) {
        List<Pokemon> matches = new ArrayList<>();
        String string = s.toLowerCase();

        for (Pokemon p : pokemons) {
            String primary = p.getPrimaryType();
            String secondary = p.getSecondaryType();

            if ((primary != null && primary.equalsIgnoreCase(string)) ||
                    (secondary != null && secondary.equalsIgnoreCase(string))) {
                matches.add(p);
            }
        }

        return matches;
    }

    /**
     * Searches the Pokémon list by Pokédex number.
     *
     * @param n the Pokédex number to search for (must be 4 digits)
     */
    public Pokemon searchByPokedexNumber(int n) {
        for (Pokemon p : pokemons) {
            if (p.getPokedexNumber() == n) {
                return p;
            }
        }
        return null;
    }

    /**
     * Handles the Pokémon search menu and lets the user choose how to search.
     */
    public void handlePokemonSearch() {
//        System.out.println("\n--- Search Pokémon ---");
//        System.out.println("1. By Name");
//        System.out.println("2. By Type");
//        System.out.println("3. By Pokédex Number");
//        System.out.print("Enter option: ");
//
//        String option = scan.nextLine();
//
//        System.out.println("");
//        switch (option) {
//            case "1":
//                String name = readValidString(scan, "name", "[A-Za-z\\s]+");
//                displaySearchResults(searchByName(name));
//                break;
//            case "2":
//                String type = readValidString(scan, "type", "[A-Za-z\\s]+");
//                displaySearchResults(searchByType(type));
//                break;
//            case "3":
//                int num = readValidInt(scan, "Pokédex number");
//                Pokemon p = searchByPokedexNumber(num);
//                if (p != null) {
//                    divider();
//                    header();
//                    divider();
//                    System.out.println(p.displayInfo());
//                } else {
//                    System.out.println("No Pokémon matched your search.");
//                }
//                break;
//            default:
//                System.out.println("Invalid option.");
//        }
        throw new UnsupportedOperationException("handlePokemonSearch needs GUI replacement");
    }

    /**
     * Validates that the given Pokédex number is in the correct range (0001 to
     * 1010).
     *
     * @param pokedexNumber the number to validate
     * @return {@code true} if valid; {@code false} otherwise
     */
    private boolean isValidDexNumber(int pokedexNumber) {
        if (pokedexNumber < 1 || pokedexNumber > 1010) {
            return false;
        }

        for (Pokemon p : pokemons) {
            if (p.getPokedexNumber() == pokedexNumber) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reads a valid string from the user that matches the given regex pattern.
     *
     * @param scan      the Scanner object to read input
     * @param attribute the name of the attribute to display in the prompt
     * @param regex     the regular expression pattern to match
     * @return a valid string input from the user
     */
    public static String readValidString(Scanner scan, String attribute, String regex) {
//        String input;
//
//        while (true) {
//            System.out.print("Enter " + attribute + ": ");
//            input = scan.nextLine().trim();
//
//            if (!input.matches(regex) || input.isEmpty()) {
//                System.out.println("Input is invalid or empty. Please try again!");
//            } else {
//                return input;
//            }
//        }
        throw new UnsupportedOperationException("readValidString needs a GUI replacement");
    }

    /**
     * Reads a valid integer from the user.
     *
     * @param scan      the Scanner object to read input
     * @param attribute the name of the attribute to display in the prompt
     * @return a valid integer input from the user
     */
    public static int readValidInt(Scanner scan, String attribute) {
//        int input;
//
//        while (true) {
//            System.out.print("Enter " + attribute + ": ");
//
//            try {
//                input = Integer.parseInt(scan.nextLine());
//                return input;
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please try again.");
//            }
//        }
        throw new UnsupportedOperationException("readValidInt needs a GUI replacement");
    }

    public boolean removeByDex(int dex) {
        boolean removed = pokemons.removeIf(p -> p.getPokedexNumber() == dex);
        // if else here
        return removed;
    }

    public void populateInitialPokemon() {
        if (pokemons.isEmpty()) {
            pokemons.add(new Pokemon(1, "Bulbasaur", "Grass", "Poison", 0, 0, 0, 0, 45, 49, 49, 45));
            pokemons.add(new Pokemon(12, "Butterfree", "Bug", "Flying", 0, 0, 0, 0, 60, 45, 50, 70));
            pokemons.add(new Pokemon(25, "Pikachu", "Electric", null, 0, 0, 0, 0, 35, 55, 40, 90));
        }
    }

}

