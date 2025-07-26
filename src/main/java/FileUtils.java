package com.pokedex.app;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;
import com.pokedex.app.Item;

public class FileUtils {

    private static final String TRAINER_FILE = "trainers.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void updateTrainerInFile(Trainer updatedTrainer) {
        List<String> lines = new ArrayList<>();
        boolean isUpdating = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINER_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    int id = Integer.parseInt(line.substring(4));
                    isUpdating = (id == updatedTrainer.getTrainerID());

                    if (isUpdating) {
                        lines.add("ID: " + updatedTrainer.getTrainerID());
                        lines.add("Name: " + updatedTrainer.getName());
                        lines.add("Birthdate: " + updatedTrainer.getBirthdate().format(FORMATTER));
                        lines.add("Gender: " + updatedTrainer.getGender());
                        lines.add("Hometown: " + updatedTrainer.getHometown());
                        lines.add("Description: " + updatedTrainer.getDescription());
                        lines.add("Money: " + updatedTrainer.getMoney());

                        List<Item> bag = updatedTrainer.getItemBag();
                        if (bag.isEmpty()) {
                            lines.add("Items: None");
                        } else {
                            StringBuilder itemsLine = new StringBuilder("Items: ");
                            for (int i = 0; i < bag.size(); i++) {
                                itemsLine.append(bag.get(i).getName());
                                if (i != bag.size() - 1) itemsLine.append(",");
                            }
                            lines.add(itemsLine.toString());
                        }

                        while ((line = reader.readLine()) != null && !line.startsWith("----")) {
                            // skip
                        }

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

        // Write updated content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAINER_FILE))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write trainers.txt: " + e.getMessage());
        }
    }
}
