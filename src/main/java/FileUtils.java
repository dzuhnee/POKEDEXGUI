package com.pokedex.app;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.app.Trainer;

public class FileUtils {

    private static final String TRAINER_FILE = "trainers.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
