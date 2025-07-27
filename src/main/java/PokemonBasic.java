package com.pokedex.app;

public class PokemonBasic {
    private int dexNumber;
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int speed;
    private String moves = ""; // ✅ Added

    public PokemonBasic(int dexNumber, String name, int hp, int attack, int defense, int speed) {
        this.dexNumber = dexNumber;
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    // ✅ Getter/Setter for moves
    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public int getDexNumber() {
        return dexNumber;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
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
}
