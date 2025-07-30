package com.pokedex.app;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;
import com.pokedex.app.TrainerManager;
import com.pokedex.app.Pokemon;
import com.pokedex.app.Item;
import com.pokedex.app.ItemRow;
import com.pokedex.app.ItemManager;
import com.pokedex.app.AppState;

public class FileUtils {
    private static final String TRAINER_FILE = "trainers.txt";
    private static final String TRAINER_ITEMS_FILE = "trainer_items.txt";
    private static final String HELD_ITEMS_FILE = "pokemon_held_items.txt";
    private static final String LINEUP_FILE = "trainer_lineup.txt";
    private static final String POKEMON_DATA_FILE = "pokemon_data.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /*
     * Reads a file line-by-line and returns the contents as a List of strings.
     * Useful for loading text-based data (e.g., trainer, item, and Pokémon data).
     */
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /*
     * Loads the trainer's items from `trainer_items.txt` and adds them to the trainer's bag.
     */
    public static void loadTrainerItemsFromFile(Trainer trainer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_ITEMS_FILE))) {
            String line;
            ItemManager itemManager = new ItemManager();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int trainerID = Integer.parseInt(parts[0].trim());
                    if (trainerID == trainer.getTrainerID()) {
                        String[] items = parts[1].split(",");
                        for (String itemName : items) {
                            Item item = itemManager.findItem(itemName.trim());
                            if (item != null) {
                                trainer.addItemToBag(item, 1);
                            }
                        }
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to read trainer_items.txt: " + e.getMessage());
        }
    }

    /*
     * Updates the trainer's basic information in `trainers.txt`.
     * Overwrites existing entry based on trainer ID and also updates the item list file.
     */
    public static void updateTrainerInFile(Trainer updatedTrainer) {
        List<String> lines = new ArrayList<>();
        boolean isUpdating = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    int id = Integer.parseInt(line.substring(4).trim());
                    isUpdating = (id == updatedTrainer.getTrainerID());

                    if (isUpdating) {
                        lines.add("ID: " + updatedTrainer.getTrainerID());
                        lines.add("Name: " + updatedTrainer.getName());
                        lines.add("Birthdate: " + updatedTrainer.getBirthdate().format(FORMATTER));
                        lines.add("Gender: " + updatedTrainer.getGender());
                        lines.add("Hometown: " + updatedTrainer.getHometown());
                        lines.add("Description: " + updatedTrainer.getDescription());
                        lines.add("Money: " + updatedTrainer.getMoney());

                        while ((line = reader.readLine()) != null && !line.startsWith("----")) {}

                        lines.add("--------------------------------------------------");
                        continue;
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to read trainers.txt: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAINER_FILE))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write trainers.txt: " + e.getMessage());
        }

        updateTrainerItemsInFile(updatedTrainer);
    }

    /*
     * Updates the item list of a trainer in `trainer_items.txt`.
     * Writes the updated list of items the trainer currently owns.
     */
    public static void updateTrainerItemsInFile(Trainer trainer) {
        List<String> lines = new ArrayList<>();
        int idToUpdate = trainer.getTrainerID();
        boolean found = false;

        try {
            File file = new File(TRAINER_ITEMS_FILE);
            if (!file.exists()) file.createNewFile();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(idToUpdate + ":")) {
                    StringBuilder itemLine = new StringBuilder(idToUpdate + ": ");
                    List<Item> items = trainer.getItemBag();
                    for (int i = 0; i < items.size(); i++) {
                        itemLine.append(items.get(i).getName());
                        if (i != items.size() - 1) {
                            itemLine.append(",");
                        }
                    }
                    lines.add(itemLine.toString());
                    found = true;
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            if (!found) {
                StringBuilder itemLine = new StringBuilder(idToUpdate + ": ");
                List<Item> items = trainer.getItemBag();
                for (int i = 0; i < items.size(); i++) {
                    itemLine.append(items.get(i).getName());
                    if (i != items.size() - 1) {
                        itemLine.append(",");
                    }
                }
                lines.add(itemLine.toString());
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Failed to update trainer_items.txt: " + e.getMessage());
        }
    }

    /*
     * Saves a Pokémon's held item in the `pokemon_held_items.txt` file.
     * If an entry already exists, it is replaced with the new one.
     */
    public static void saveHeldItem(int trainerID, String pokemonName, String itemName) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try {
            File file = new File(HELD_ITEMS_FILE);
            if (!file.exists()) file.createNewFile();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(trainerID + ":") && line.contains("->" + pokemonName + ":")) {
                    lines.add(trainerID + ":" + pokemonName + ":" + itemName);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            if (!found) {
                lines.add(trainerID + ":" + pokemonName + ":" + itemName);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Failed to update held items: " + e.getMessage());
        }
    }

    /*
     * Loads the held item of a Pokémon from `pokemon_held_items.txt`.
     * Returns "None" if no item is found.
     */
    public static String loadHeldItem(int trainerID, String pokemonName) {
        System.out.println("Looking for held items at: " + new File(HELD_ITEMS_FILE).getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(HELD_ITEMS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(trainerID + ":") && line.contains(":" + pokemonName + ":")) {
                    String[] parts = line.split(":");
                    if (parts.length == 3) {
                        return parts[2].trim();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read held items: " + e.getMessage());
        }
        return "None";
    }

    /*
     * Loads basic information about a trainer (name, gender, money, etc.) using the trainer's ID.
     * Does not include lineup or item data.
     */
    public static Trainer loadTrainerBasicInfo(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_FILE))) {
            String line;
            int tempID = -1;
            String name = "";
            String gender = "";
            String hometown = "";
            String description = "";
            int money = 0;
            java.time.LocalDate birthdate = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID:")) {
                    tempID = Integer.parseInt(line.substring(3).trim());
                } else if (line.startsWith("Name:")) {
                    name = line.substring(5).trim();
                } else if (line.startsWith("Birthdate:")) {
                    birthdate = java.time.LocalDate.parse(line.substring(10).trim());
                } else if (line.startsWith("Gender:")) {
                    gender = line.substring(7).trim();
                } else if (line.startsWith("Hometown:")) {
                    hometown = line.substring(9).trim();
                } else if (line.startsWith("Description:")) {
                    description = line.substring(12).trim();
                } else if (line.startsWith("Money:")) {
                    money = Integer.parseInt(line.substring(6).trim());
                } else if (line.startsWith("--------------------------------------------------")) {
                    if (tempID == id) {
                        return new Trainer(tempID, name, birthdate, gender, hometown, description, money);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load trainer basic info: " + e.getMessage());
        }
        return null;
    }

    /*
     * Loads the list of Pokémon currently in a trainer’s lineup using their trainer ID.
     * Pokémon data is loaded from `trainer_lineup.txt` and `pokemon_data.txt`.
     */
    public static List<Pokemon> loadTrainerPokemon(int id) {
        List<Pokemon> lineup = new ArrayList<>();
        Trainer trainer = loadTrainerBasicInfo(id); // get name from ID
        if (trainer == null) return lineup;

        String trainerName = trainer.getName().trim();
        List<String> lines = readFile(LINEUP_FILE);

        for (String line : lines) {
            String[] split = line.split("->");
            if (split.length < 2) continue;

            String name = split[0].trim();
            String pokemonData = split[1].trim();

            if (name.equalsIgnoreCase(trainerName)) {
                String[] pokeParts = pokemonData.split(",");
                if (pokeParts.length < 2) continue;

                int pokemonId = Integer.parseInt(pokeParts[0].trim());
                Pokemon pokemon = createPokemonFromDataFile(pokemonId);
                if (pokemon != null) {
                    lineup.add(pokemon);
                }
            }
        }

        return lineup;
    }

    /*
     * Loads the list of items owned by the trainer using their trainer ID.
     * Returns a list of Item objects.
     */
    public static List<Item> loadTrainerItems(int id) {
        List<Item> items = new ArrayList<>();
        ItemManager itemManager = new ItemManager();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_ITEMS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && Integer.parseInt(parts[0].trim()) == id) {
                    String[] itemNames = parts[1].split(",");
                    for (String name : itemNames) {
                        Item item = itemManager.findItem(name.trim());
                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load trainer items: " + e.getMessage());
        }

        return items;
    }

    /*
     * Loads a specific Pokémon's full data using its Pokédex number.
     * Returns a new Pokemon object if found; otherwise, returns null.
     */
    public static Pokemon loadPokemonByDex(int dexNumber) {
        List<String> lines = readFile(POKEMON_DATA_FILE);
        for (String line : lines) {
            String[] tokens = line.split(",");
            int pokedexNumber = Integer.parseInt(tokens[0]);

            if (pokedexNumber == dexNumber) {
                String name = tokens[1];
                String primary = tokens[2];
                String secondary = tokens[3].equalsIgnoreCase("None") ? null : tokens[3];
                int baseLevel = Integer.parseInt(tokens[4]);
                int evolvesTo = Integer.parseInt(tokens[5]);
                int evolvesFrom = Integer.parseInt(tokens[6]);
                int evolutionLevel = Integer.parseInt(tokens[7]);
                int hp = Integer.parseInt(tokens[8]);
                int atk = Integer.parseInt(tokens[9]);
                int def = Integer.parseInt(tokens[10]);
                int spd = Integer.parseInt(tokens[11]);

                if (secondary == null) {
                    return new Pokemon(pokedexNumber, name, primary, baseLevel, evolvesFrom, evolvesTo, evolutionLevel, hp, atk, def, spd);
                } else {
                    return new Pokemon(pokedexNumber, name, primary, secondary, baseLevel, evolvesFrom, evolvesTo, evolutionLevel, hp, atk, def, spd);
                }
            }
        }
        return null;
    }

    /*
     * Converts a trainer's item bag into a list of ItemRow objects.
     * Groups identical items and counts their quantity for display purposes.
     */
    public static List<ItemRow> getItemRows(Trainer trainer) {
        List<ItemRow> itemRows = new ArrayList<>();
        List<Item> bag = trainer.getItemBag();
        List<String> counted = new ArrayList<>();

        for (Item item : bag) {
            String itemName = item.getName();

            if (!counted.contains(itemName)) {
                // Count how many times this item appears in the bag
                int count = 0;
                for (Item i : bag) {
                    if (i.getName().equals(itemName)) {
                        count++;
                    }
                }

                // Add to result and mark as counted
                itemRows.add(new ItemRow(item, count));
                counted.add(itemName);
            }
        }

        return itemRows;
    }

    /*
     * Creates a Pokémon object using data from `pokemon_data.txt` based on Pokédex ID.
     * Parses stats, types, evolution data, and other attributes.
     */
    public static Pokemon createPokemonFromDataFile(int id) {
        List<String> lines = readFile(POKEMON_DATA_FILE);

        for (String line : lines) {
            String[] parts = line.split(",");
            int pokeId = Integer.parseInt(parts[0]);
            if (pokeId == id) {
                String name = parts[1];
                String type1 = parts[2];
                String type2 = parts[3];
                int baseLevel = Integer.parseInt(parts[4]);
                int evolveLevel = Integer.parseInt(parts[5]);
                int preEvoId = Integer.parseInt(parts[6]);
                int nextEvoId = Integer.parseInt(parts[7]);
                int hp = Integer.parseInt(parts[8]);
                int attack = Integer.parseInt(parts[9]);
                int defense = Integer.parseInt(parts[10]);
                int speed = Integer.parseInt(parts[11]);

                Pokemon pokemon = new Pokemon(
                        pokeId, name, type1, type2.equals("None") ? null : type2,
                        baseLevel, preEvoId, nextEvoId, evolveLevel,
                        hp, attack, defense, speed
                );

                pokemon.setBaseLevel(baseLevel);
                return pokemon;
            }
        }
        return null;
    }

    /*
     * Loads a trainer's lineup using their name.
     * Also loads each Pokémon's held item if any is found in `pokemon_held_items.txt`.
     */
    public static List<Pokemon> loadLineupFromFile(String trainerName) {
        List<Pokemon> lineup = new ArrayList<>();
        List<String> lines = readFile(LINEUP_FILE);
        Trainer trainer = TrainerManager.findByName(trainerName);
        if (trainer == null) return lineup;

        for (String line : lines) {
            String[] split = line.split("->");
            if (split.length < 2) continue;

            String name = split[0].trim();
            String pokemonData = split[1].trim();

            if (name.equalsIgnoreCase(trainerName)) {
                String[] pokeParts = pokemonData.split(",");
                for (String pokeIDStr : pokeParts) {
                    int pokeID = Integer.parseInt(pokeIDStr.trim());
                    Pokemon p = createPokemonFromDataFile(pokeID);

                    if (p != null) {
                        // Load held item from file
                        String heldItemName = loadHeldItem(trainer.getTrainerID(), p.getName());
                        if (!"None".equals(heldItemName)) {
                            ItemManager itemManager = new ItemManager();
                            Item heldItem = itemManager.findItem(heldItemName);
                            if (heldItem != null) {
                                p.setHeldItem(heldItem);
                            }
                        }
                        lineup.add(p);
                    }
                }
            }
        }
        return lineup;
    }
}
