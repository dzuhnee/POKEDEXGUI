package com.pokedex.app;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;
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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public static List<Pokemon> loadTrainerPokemon(int id) {
        List<Pokemon> pokemons = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LINEUP_FILE))) {
            String line;
            Trainer fullTrainer = AppState.getInstance().getFullTrainer();
            if (fullTrainer == null) {
                System.err.println("AppState fullTrainer is null. Cannot load Pokémon lineup.");
                return pokemons;
            }

            String trainerName = fullTrainer.getName();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(trainerName + " -> ")) {
                    String[] parts = line.split("->")[1].trim().split(",");
                    int pokedexNumber = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    Pokemon p = new Pokemon(pokedexNumber, name, "", "", 1, -1, -1, -1, 0, 0, 0, 0);

                    String heldItemName = loadHeldItem(id, name);
                    if (!heldItemName.equals("None")) {
                        Item item = new ItemManager().findItem(heldItemName);
                        if (item != null) {
                            p.setHeldItem(item);
                        }
                    }

                    pokemons.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load trainer Pokémon: " + e.getMessage());
        }
        return pokemons;
    }

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

    public static Pokemon loadPokemonByDex(int dexNumber) {
        File file = new File("pokemon_data.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
        } catch (IOException e) {
            e.printStackTrace(); // or show GUI alert
        }

        return null;
    }

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


}
