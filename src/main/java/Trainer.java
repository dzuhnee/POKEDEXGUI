package com.pokedex.app;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.pokedex.app.Pokemon;
import com.pokedex.app.Item;
import com.pokedex.app.PokemonManager;
import com.pokedex.app.Move;

public class Trainer {
    // Attributes
    private int trainerID;
    private String name;
    private LocalDate birthdate;
    private String sex;
    private String hometown;
    private String description;
    private int money;

    private List<Pokemon> lineup; // Up to 6
    private List<Pokemon> storage; // Storage for additional pokemon
    private List<Item> itemBag; // Up to 50 (max 10 unique)
    private final int MAX_BAG_SIZE = 50;
    private final int MAX_UNIQUE_TYPES = 10;

    // Constructor
    public Trainer(int trainerId, String name, LocalDate birthdate, String sex, String hometown, String description) {
        this.trainerID = trainerId;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.hometown = hometown;
        this.description = description;
        this.money = 1000000;

        this.lineup = new ArrayList<>();
        this.storage = new ArrayList<>();
        this.itemBag = new ArrayList<>();
    }

    // Getters
    public int getTrainerID() {
        return trainerID;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return sex;
    }

    public String getHometown() {
        return hometown;
    }

    public String getDescription() {
        return description;
    }

    public int getMoney() {
        return money;
    }

    public List<Pokemon> getLineup() {
        return lineup;
    }

    public List<Pokemon> getStorage() {
        return storage;
    }

    public List<Item> getItemBag() {
        return new ArrayList<>(itemBag);
    }

    // Setter for money
    public void addMoney(int amount) {
        if (amount > 0) {
            this.money += amount;
        }
    }

    public boolean subtractMoney(int amount) {
        if (amount > 0 && this.money >= amount) {
            this.money -= amount;
            return true;
        }
        return false;
    }

    // Methods

    public int getItemQuantity(String itemName) {
        int ctr = 0;
        for (Item item : itemBag) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                ctr++;
            }
        }
        return ctr;
    }

    public int getTotalItemCount() {
        return itemBag.size();
    }

    public int getUniqueItemTypeCount() {
        List<String> uniqueNames = new ArrayList<>();

        for (Item item : itemBag) {
            if (!uniqueNames.contains(item.getName())) {
                uniqueNames.add(item.getName());
            }
        }
        return uniqueNames.size();
    }

    public boolean addItemToBag(Item item, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        if (getItemQuantity(item.getName()) == 0 && getUniqueItemTypeCount() >= MAX_UNIQUE_TYPES) {
            return false; // GUI will need to display this message
        }

        if (getTotalItemCount() + quantity > MAX_BAG_SIZE) {
            // GUI will need to display this message and potentially prompt for discard
            return false;
        }

        for (int i = 0; i < quantity; i++) {
            itemBag.add(item);
        }
        return true;
    }

    public boolean removeItemFromBag(String itemName, int quantity) {
        if (quantity <= 0) return false;

        if (getItemQuantity(itemName) < quantity) {
            return false; // Not enough items to remove
        }

        int removedCount = 0;
        for (int i = itemBag.size() - 1; i >= 0 && removedCount < quantity; i--) {
            if (itemBag.get(i).getName().equalsIgnoreCase(itemName)) {
                itemBag.remove(i);
                removedCount++;
            }
        }
        return removedCount == quantity;
    }

    public boolean processPurchase(Item shopItem, int quantity) {
        int totalCost = shopItem.getBuyingPrice() * quantity;
        if (totalCost == -1 || totalCost > money) {
            return false; // Item not for sale or not enough money
        }

        if (getItemQuantity(shopItem.getName()) == 0 && getUniqueItemTypeCount() >= MAX_UNIQUE_TYPES) {
            return false; // GUI handles message
        }

        if (getTotalItemCount() + quantity > MAX_BAG_SIZE) {
            return false; // GUI handles message and discard prompt
        }

        if (subtractMoney(totalCost)) {
            for (int i = 0; i < quantity; i++) {
                itemBag.add(shopItem);
            }
            return true;
        }
        return false;
    }

    public boolean processSale(Item shopItem, int quantity) {
        int totalProceeds = shopItem.getSellingPrice() * quantity;

        if (totalProceeds == -1 || getItemQuantity(shopItem.getName()) < quantity) {
            return false;
        }

        if (removeItemFromBag(shopItem.getName(), quantity)) {
            addMoney(totalProceeds);
            return true;
        }
        return false;
    }

    public boolean useItem(Item item, Pokemon pokemon, PokemonManager pokemonManager) {
        if (item == null || pokemon == null || pokemonManager == null) {
            return false;
        }

        if (getItemQuantity(item.getName()) <= 0) {
            return false;
        }

        item.use(pokemon, pokemonManager);

        boolean itemConsumed = true;

        if (itemConsumed) {
            return removeItemFromBag(item.getName(), 1);
        }
        return true; // Item used, but not consumed (e.g., a held item being applied)
    }

    public boolean teachMove(Pokemon pokemon, Move move) {
        return pokemon.learnMove(move);
    }

    public boolean addPokemonToLineup(Pokemon p) {
        if (lineup.size() < 6) {
            lineup.add(p);
            return true;
        } else {
            storage.add(p);
            return false; // Pokemon added to storage, not lineup
        }
    }

    public boolean switchToLineup(Pokemon p) {
        if (lineup.size() < 6 && storage.contains(p)) {
            storage.remove(p);
            lineup.add(p);
            return true;
        }
        return false;
    }

    public boolean switchToStorage(Pokemon p) {
        if (lineup.contains(p)) {
            lineup.remove(p);
            storage.add(p);
            return true;
        }
        return false;
    }

    public boolean releasePokemon(Pokemon p) {
        return lineup.remove(p) || storage.remove(p);
    }

    /*
     * private int trainerID;
     * private String name;
     * private LocalDate birthdate;
     * private String sex;
     * private String hometown;
     * private String description;
     * private int money;
     *
     * private List<Pokemon> lineup; // Up to 6
     * private List<Pokemon> storage; // Storage for additional pokemon
     * private List<Item> itemBag; // Up to 50 (max 10 unique)
     */
    public String displayProfile() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("ID         : %04d\n", trainerID));
        sb.append(String.format("Name       : %s\n", name));
        sb.append(String.format("Sex        : %s\n", sex));
        sb.append(String.format("Birthdate  : %s\n", birthdate.toString()));
        sb.append(String.format("Hometown   : %s\n", hometown));
        sb.append(String.format("Money      : ₱%,d\n", money));
        sb.append(String.format("Description: %s\n", description));

        return sb.toString();
    }

}
