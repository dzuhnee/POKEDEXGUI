package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Item;
import com.pokedex.app.PokemonManager;
import com.pokedex.app.Move;

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

    private int hp;
    private int attack;
    private int defense;
    private int speed;

    public Pokemon(int pokedexNumber, String name, String primaryType, String secondaryType, int baseLevel,
                   int evolvesFrom, int evolvesTo, int evolutionLevel, int hp, int attack, int defense, int speed) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.baseLevel = baseLevel;
        this.evolvesFrom = evolvesFrom;
        this.evolvesTo = evolvesTo;
        this.evolutionLevel = evolutionLevel;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.moveSet = new ArrayList<>();
    }

    public Pokemon(int pokedexNumber, String name, String primaryType, int baseLevel, int evolvesFrom, int evolvesTo,
                   int evolutionLevel, int hp, int attack, int defense, int speed) {
        this(pokedexNumber, name, primaryType, null, baseLevel, evolvesFrom, evolvesTo, evolutionLevel,
                hp, attack, defense, speed);
    }

    public String getName() {
        return name;
    }

    public int getPokedexNumber() {
        return pokedexNumber;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public String getSecondaryType() {
        return secondaryType;
    }

    public int getEvolvesFrom() {
        return evolvesFrom;
    }

    public int getEvolvesTo() {
        return evolvesTo;
    }

    public int getEvolutionLevel() {
        return evolutionLevel;
    }

    public int getHP() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setBaseLevel(int level) {
        this.baseLevel = level;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    public String getHeldItemName() {
        return (heldItem == null) ? "None" : heldItem.getName();
    }

    public List<Move> getMoveSet() {
        return new ArrayList<>(moveSet);
    }

    public boolean learnMove(Move move) {
        if (!isTypeCompatible(move)) return false;
        if (moveSet.contains(move)) return false;
        if (moveSet.size() < 4) {
            moveSet.add(move);
            return true;
        }
        return false;
    }

    public void forgetMove(Move move) {
        if (!moveSet.contains(move)) return;
        if (move.getClassification() != Move.Classification.HM) moveSet.remove(move);
    }

    public void increaseStat(String statName, int amount) {
        switch (statName.toLowerCase()) {
            case "hp": this.hp += amount; break;
            case "attack": this.attack += amount; break;
            case "defense": this.defense += amount; break;
            case "speed": this.speed += amount; break;
        }
    }

    public boolean levelUpWithRareCandy(PokemonManager manager) {
        this.baseLevel++;
        this.hp += this.hp * 0.10;
        this.attack += this.attack * 0.10;
        this.defense += this.defense * 0.10;
        this.speed += this.speed * 0.10;

        if (this.baseLevel >= this.evolutionLevel && this.evolvesTo != 0) {
            return evolve(manager);
        }
        return false;
    }

    public boolean evolve(PokemonManager manager) {
        Pokemon evolved = manager.getPokemonByDex(this.evolvesTo);
        if (evolved == null) return false;

        // Transfer properties
        evolved.setBaseLevel(this.baseLevel); // carry over new level
        evolved.setHeldItem(this.heldItem);   // keep held item if any

        // Replace Pokémon’s identity with the evolved one
        this.name = evolved.getName();
        this.primaryType = evolved.getPrimaryType();
        this.secondaryType = evolved.getSecondaryType();
        this.evolutionLevel = evolved.getEvolutionLevel();
        this.evolvesTo = evolved.getEvolvesTo();
        this.hp = evolved.getHP();
        this.attack = evolved.getAttack();
        this.defense = evolved.getDefense();
        this.speed = evolved.getSpeed();

        return true;
    }

    public boolean evolveUsingStone(String stoneType, PokemonManager manager) {
        if (!TypeUtils.isValidType(stoneType)) return false;
        if (this.evolvesTo == 0) return false;

        Pokemon evolved = manager.getPokemonByDex(this.evolvesTo);
        if (evolved == null) return false;

        if (!evolved.getPrimaryType().equalsIgnoreCase(stoneType)
                && (evolved.getSecondaryType() == null || !evolved.getSecondaryType().equalsIgnoreCase(stoneType))) {
            return false;
        }

        return evolve(manager);
    }

    public String useItem(Item item, PokemonManager manager) {
        return item.use(this, manager);
    }

    public String cry() {
        String sound = " cries!";
        if (name.toLowerCase().contains("chu")) {
            sound = " squeaks: Pika Pika~!";
        } else if (primaryType != null) {
            switch (primaryType.toLowerCase()) {
                case "fire": sound = " roars: Fwooosh 🔥!"; break;
                case "water": sound = " splashes: Bloo bloop 💧!"; break;
                case "electric": sound = " zaps: Bzzzt ⚡!"; break;
                case "grass": sound = " rustles: Shwaa 🌿!"; break;
                case "ghost": sound = " wails: Woooo 👻..."; break;
                default: sound = " cries!";
            }
        }
        return this.name + sound;
    }

    public String getHeldItemNameProperty() {
        return getHeldItemName();
    }

    public String getNameProperty() {
        return name;
    }

    private boolean isTypeCompatible(Move move) {
        if (primaryType != null && primaryType.equalsIgnoreCase(move.getPrimaryType())) return true;
        if (primaryType != null && move.getSecondaryType() != null && primaryType.equalsIgnoreCase(move.getSecondaryType())) return true;
        if (secondaryType != null && secondaryType.equalsIgnoreCase(move.getPrimaryType())) return true;
        if (secondaryType != null && move.getSecondaryType() != null && secondaryType.equalsIgnoreCase(move.getSecondaryType())) return true;
        return false;
    }

    public Pokemon cloneForPreview() {
        Pokemon clone = new Pokemon(
                this.pokedexNumber, this.name, this.primaryType, this.secondaryType,
                this.baseLevel, this.evolvesFrom, this.evolvesTo, this.evolutionLevel,
                this.hp, this.attack, this.defense, this.speed
        );
        clone.setHeldItem(this.getHeldItem()); // optional
        return clone;
    }

}
