package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pokedex.app.Item;
import com.pokedex.app.PokemonManager;
import com.pokedex.app.Move;

/**
 * This class represents a Pokémon with identifying information, types,
 * evolution details,
 * stats, moves, and held item.
 */
public class Pokemon {
    private int pokedexNumber;
    private String name;
    private String primaryType;
    private String secondaryType;
    private int baseLevel;
    private int evolvesFrom;
    private int evolvesTo;
    private int evolutionLevel;
    private List<Move> moveSet;
    private Item heldItem = null;
    private final PokemonBaseStats baseStats;

    /**
     * Constructs a dual-type Pokémon with specified information.
     *
     * @param pokedexNumber  the Pokédex number of the Pokémon
     * @param name           the name of the Pokémon
     * @param primaryType    the primary type of the Pokémon
     * @param secondaryType  the secondary type of the Pokémon
     * @param baseLevel      the base level of the Pokémon
     * @param evolvesFrom    the Pokédex number it evolves from
     * @param evolvesTo      the Pokédex number it evolves to
     * @param evolutionLevel the level at which it evolves
     * @param hp             the base HP stat
     * @param attack         the base Attack stat
     * @param defense        the base Defense stat
     * @param speed          the base Speed stat
     */
    public Pokemon(int pokedexNumber, String name, String primaryType, String secondaryType, int baseLevel,
                   int evolvesFrom,
                   int evolvesTo, int evolutionLevel, int hp, int attack, int defense, int speed) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.baseLevel = baseLevel;
        this.evolvesFrom = evolvesFrom;
        this.evolvesTo = evolvesTo;
        this.evolutionLevel = evolutionLevel;
        this.baseStats = new PokemonBaseStats(hp, attack, defense, speed);

        this.heldItem = null;
        this.moveSet = new ArrayList<>();
    }

    /**
     * Constructs a single-type Pokémon (no secondary type).
     *
     * @param pokedexNumber  the Pokédex number of the Pokémon
     * @param name           the name of the Pokémon
     * @param primaryType    the primary type of the Pokémon
     * @param baseLevel      the base level of the Pokémon
     * @param evolvesFrom    the Pokédex number it evolves from
     * @param evolvesTo      the Pokédex number it evolves to
     * @param evolutionLevel the level at which it evolves
     * @param hp             the base HP stat
     * @param attack         the base Attack stat
     * @param defense        the base Defense stat
     * @param speed          the base Speed stat
     */
    public Pokemon(int pokedexNumber, String name, String primaryType, int baseLevel, int evolvesFrom, int evolvesTo,
                   int evolutionLevel, int hp, int attack, int defense, int speed) {
        this(pokedexNumber, name, primaryType, null, baseLevel, evolvesFrom, evolvesTo, evolutionLevel,
                hp, attack, defense, speed);
    }

    /**
     * Gets the name of the Pokémon.
     *
     * @return the Pokémon's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Pokédex number.
     *
     * @return the Pokédex number
     */
    public int getPokedexNumber() {
        return pokedexNumber;
    }

    /**
     * Gets the base level of the Pokémon.
     *
     * @return the base level
     */
    public int getBaseLevel() {
        return baseLevel;
    }

    /**
     * Gets the primary type of the Pokémon.
     *
     * @return the primary type
     */
    public String getPrimaryType() {
        return primaryType;
    }

    /**
     * Gets the secondary type of the Pokémon.
     *
     * @return the secondary type, or null if none
     */
    public String getSecondaryType() {
        return secondaryType;
    }

    /**
     * Gets the Pokédex number it evolves from.
     *
     * @return the Pokédex number it evolves from
     */
    public int getEvolvesFrom() {
        return evolvesFrom;
    }

    /**
     * Gets the Pokédex number it evolves to.
     *
     * @return the Pokédex number it evolves to
     */
    public int getEvolvesTo() {
        return evolvesTo;
    }

    /**
     * Gets the evolution level of the Pokémon.
     *
     * @return the level at which it evolves
     */
    public int getEvolutionLevel() {
        return evolutionLevel;
    }

    /**
     * Gets the base HP stat.
     *
     * @return the HP stat
     */
    public int getHP() {
        return baseStats.getHP();
    }

    /**
     * Gets the base Attack stat.
     *
     * @return the Attack stat
     */
    public int getAttack() {
        return baseStats.getAttack();
    }

    /**
     * Gets the base Defense stat.
     *
     * @return the Defense stat
     */
    public int getDefense() {
        return baseStats.getDefense();
    }

    /**
     * Gets the base Speed stat.
     *
     * @return the Speed stat
     */
    public int getSpeed() {
        return baseStats.getSpeed();
    }

    /**
     * Gets the move set of the Pokémon.
     *
     * @return a copy of the move set list
     */
    public List<Move> getMoveSet() {
        return new ArrayList<>(moveSet);
    }

    /**
     * Gets the held item of the Pokémon.
     *
     * @return the held item, or null if none
     */

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    /**
     * Simulates the Pokémon crying (prints a simple message).
     */
    public String cry() {
        String sound = " cries!";

        if (name.toLowerCase().contains("chu")) {
            sound = " squeaks: Pika Pika~!";
        } else if (primaryType != null) {
            switch (primaryType.toLowerCase()) {
                case "fire":
                    sound = " roars: Fwooosh 🔥!";
                    break;
                case "water":
                    sound = " splashes: Bloo bloop 💧!";
                    break;
                case "electric":
                    sound = " zaps: Bzzzt ⚡!";
                    break;
                case "grass":
                    sound = " rustles: Shwaa 🌿!";
                    break;
                case "ghost":
                    sound = " wails: Woooo 👻...";
                    break;
                default:
                    sound = " cries!";
            }
        }

        return this.name + sound;
    }

    public boolean learnMove(Move move) {
        // Check if type is compatible
        if (!isTypeCompatible(move)) {
            return false; // For GUI: Display "name cannot learn move"
        }

        // Check if move is in the set already
        if (this.moveSet.contains(move)) {
            return false; // For GUI: Display "name already knows move"
        }

        // Check if adding a new move will exceed the limit
        if (this.moveSet.size() < 4) {
            this.moveSet.add(move);
            return true; // For GUI: Display "name learned move successfully"
        } else {
            return false; // For GUI: Display "name alr knows 4 moves. Do you want to forget bla bla"
        }
    }

    public void forgetMove(Move move) {
        if (!moveSet.contains(move)) {
            return; // GUI: name doesnt know move
        }

        if (move.getClassification() != Move.Classification.HM) {
            moveSet.remove(move); // success message
        } // otherwise, "move is an HM and cannot be forgotten"
    }

    // add display messages sa GUI na lang
    public void increaseStat(String statName, int amount) {
        switch (statName.toLowerCase()) {
            case "hp":
                baseStats.setHP(amount);
                break;
            case "attack":
                baseStats.setAttack(amount);
                break;
            case "defense":
                baseStats.setDefense(amount);
                break;
            case "speed":
                baseStats.setSpeed(amount);
                break;
        }
    }

    public boolean levelUpWithRareCandy(PokemonManager manager) {
        this.baseLevel++;

        int newHP = (int) (baseStats.getHP() * 0.10);
        int newAttack = (int) (baseStats.getAttack() * 0.10);
        int newDefense = (int) (baseStats.getDefense() * 0.10);
        int newSpeed = (int) (baseStats.getSpeed() * 0.10);

        baseStats.addHP(newHP);
        baseStats.addAttack(newAttack);
        baseStats.addDefense(newDefense);
        baseStats.addSpeed(newSpeed);

        if (this.baseLevel >= this.evolutionLevel && this.evolvesTo != 0) {
            return evolve(manager);
        }
        return false;
    }

    /**
     * Displays the Pokémon's details including name, types, and base stats in a
     * formatted line.
     */
    public String displayInfo() {
        String types = primaryType;

        if (secondaryType != null && !secondaryType.isEmpty()) {
            types += "/" + secondaryType;
        }

        return String.format("%04d %-12s %-15s %-7d %-5d %-7d %-8d %-6d\n", pokedexNumber, name, types,
                baseStats.getTotal(), baseStats.getHP(), baseStats.getAttack(), baseStats.getDefense(),
                baseStats.getSpeed());
    }

    private boolean isTypeCompatible(Move move) {
        // Check Type 1
        if (this.primaryType != null && this.primaryType.equalsIgnoreCase(move.getPrimaryType())) {
            return true;
        }
        if (this.primaryType != null && move.getSecondaryType() != null
                && this.primaryType.equalsIgnoreCase(move.getSecondaryType())) {
            return true;
        }

        // Check Type 2 (if it exists!)
        if (this.secondaryType != null && this.secondaryType.equalsIgnoreCase(move.getPrimaryType())) {
            return true;
        }
        if (this.secondaryType != null && move.getSecondaryType() != null
                && this.secondaryType.equalsIgnoreCase(move.getSecondaryType())) {
            return true;
        }

        return false;
    }

    private boolean evolve(PokemonManager manager) {
        Pokemon evolved = manager.getPokemonByDex(this.evolvesTo);

        if (evolved == null) {
            return false; // Error Message
        }

        // name is evolving...!
        this.name = evolved.getName();
        this.evolvesFrom = this.pokedexNumber;
        this.pokedexNumber = evolved.getPokedexNumber();
        this.primaryType = evolved.getPrimaryType();
        this.secondaryType = evolved.getSecondaryType();
        this.evolvesTo = evolved.getEvolvesTo();
        this.evolutionLevel = evolved.getEvolutionLevel();

        this.baseStats.setHP(evolved.getHP());
        this.baseStats.setAttack(evolved.getAttack());
        this.baseStats.setDefense(evolved.getDefense());
        this.baseStats.setSpeed(evolved.getSpeed());

        // Congratulatory message
        return true;
    }

    public boolean evolveUsingStone(String stoneType, PokemonManager manager) {
        if (!TypeUtils.isValidType(stoneType)) {
            return false; // error msg
        }

        if (this.evolvesTo == 0) {
            return false; // informative msg
        }

        // Get the evolved Pokémon data
        Pokemon evolved = manager.getPokemonByDex(this.evolvesTo);
        if (evolved == null) {
            return false;
        }

        // Evolution is allowed only if the evolved Pokémon shares the stone type
        if (!evolved.getPrimaryType().equalsIgnoreCase(stoneType)
                && (evolved.getSecondaryType() == null || !evolved.getSecondaryType().equalsIgnoreCase(stoneType))) {
            return false;
        }

        // Proceed with evolution
        return evolve(manager);
    }

    public void useItem(Item item, PokemonManager manager) {
        if (item != null) {
            item.use(this, manager);
        } // else display an informative message
    }

}

/**
 * This class represents the base stats of a Pokémon, including HP, Attack,
 * Defense, Special Attack,
 * Special Defense, and Speed.
 */
class PokemonBaseStats {
    private int hp;
    private int attack;
    private int defense;
    private int speed;

    /**
     * Constructs a PokemonBaseStats object with the given base stat values.
     *
     * @param hp      the base HP (Hit Points) stat of the Pokémon
     * @param attack  the base Attack stat of the Pokémon
     * @param defense the base Defense stat of the Pokémon
     * @param speed   the base Speed stat of the Pokémon
     */
    public PokemonBaseStats(int hp, int attack, int defense, int speed) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    /**
     * Gets the base HP stat.
     *
     * @return the base HP value
     */
    public int getHP() {
        return hp;
    }

    /**
     * Gets the base Attack stat.
     *
     * @return the base Attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Gets the base Defense stat.
     *
     * @return the base Defense value
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Gets the base Speed stat.
     *
     * @return the base Speed value
     */
    public int getSpeed() {
        return speed;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addHP(int amount) {
        this.hp += amount;
    }

    public void addAttack(int amount) {
        this.attack += amount;
    }

    public void addDefense(int amount) {
        this.defense += amount;
    }

    public void addSpeed(int amount) {
        this.speed += amount;
    }

    /**
     * Calculates and returns the total of all base stats.
     *
     * @return the total of HP, Attack, Defense, Special Attack, Special Defense,
     *         and Speed
     */
    public int getTotal() {
        return hp + attack + defense + speed;
    }

    /**
     * Prompts the user to enter a valid base stat value for a given attribute.
     * The input must be an integer between 1 and 255 (inclusive).
     * If the input is invalid, the user will be prompted again until a valid value
     * is entered.
     *
     * @param scan      the Scanner object used to read user input
     * @param attribute the name of the attribute being set (e.g., "HP", "Attack")
     * @return a valid integer value between 1 and 255 for the specified attribute
     */
    public static int readValidBaseStat(Scanner scan, String attribute) {
//        int input;
//
//        do {
//            input = PokemonManager.readValidInt(scan, attribute);
//            if (input < 1 || input > 255) {
//                System.out.println(attribute + " must be between 1-255 only!");
//            }
//        } while (input < 1 || input > 255);
//
//        return input;
        throw new UnsupportedOperationException("readValidBaseStat needs a GUI replacement.");
    }
}
