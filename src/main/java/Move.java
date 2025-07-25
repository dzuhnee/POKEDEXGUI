package com.pokedex.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pokémon move with a name, description, classification (HM/TM),
 * and types (primary and optional secondary).
 */
public class Move {

    private final String name;
    private final String description;
    private final Classification classification;
    private final String primaryType;
    private final String secondaryType;

    /**
     * Constructs a Move with the specified details.
     *
     * @param name           the name of the move
     * @param description    the description or effect of the move
     * @param classification whether the move is an HM or TM
     * @param primaryType    the primary type of the move (e.g., Fire, Water)
     * @param secondaryType  the secondary type of the move (optional, can be empty)
     */
    public Move(String name, String description, Classification classification, String primaryType,
                String secondaryType) {
        this.name = name;
        this.description = description;
        this.classification = classification;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }

    // Getters

    /**
     * @return the name of the move
     */
    public String getName() {
        return name;
    }

    /**
     * @return the move's description or effect
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the classification of the move (HM or TM)
     */
    public Classification getClassification() {
        return classification;
    }

    /**
     * @return the move's primary type
     */
    public String getPrimaryType() {
        return primaryType;
    }

    /**
     * @return the move's secondary type (can be empty)
     */
    public String getSecondaryType() {
        return secondaryType;
    }

    /**
     * Enum representing a move's classification: either HM or TM.
     */
    public enum Classification {
        HM,
        TM
    }

    public String displayInfo() {
        StringBuilder info = new StringBuilder();

        info.append(String.format("Name           : %s\n", name));
        info.append(String.format("Classification : %s\n", classification.name())); // .name() to get String from enum
        info.append(String.format("Primary Type   : %s\n", primaryType));
        if (secondaryType != null && !secondaryType.isEmpty()) {
            info.append(String.format("Secondary Type : %s\n", secondaryType));
        }
        info.append(String.format("Description    : %s\n", description));

        return info.toString();
    }
}


/**
 * Manages a collection of Pokémon moves, including adding, viewing, and
 * searching.
 */
class MoveManager {

    private final List<Move> moveList;

    public MoveManager() {
        moveList = new ArrayList<>();
        loadDefaultMoves();
    }

    /**
     * Prompts the user to input a new move and adds it to the list if valid.
     */
    public boolean addMove(String name, String description, Move.Classification classification, String primaryType, String secondaryType) {
        if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty() ||
                primaryType == null || primaryType.trim().isEmpty() || classification == null) {
            return false;
        }

        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        String formattedDescription = description.substring(0, 1).toUpperCase() + description.substring(1);
        if (!formattedDescription.endsWith(".")) {
            formattedDescription += "."; // Ensure consistency
        }
        String formattedPrimaryType = primaryType.substring(0, 1).toUpperCase() + primaryType.substring(1).toLowerCase();
        String formattedSecondaryType = (secondaryType != null && !secondaryType.isEmpty()) ?
                (secondaryType.substring(0, 1).toUpperCase() + secondaryType.substring(1).toLowerCase()) : null;


        if (!formattedName.matches("[A-Za-z0-9.\\-\\s'()]+")) {
            return false;
        }
        if (!formattedDescription.matches("[A-Za-z0-9.,'\\-()/\\%\\s]+")) {
            return false;
        }
        if (isMoveNameTaken(formattedName)) {
            return false; // Name already exists
        }
        if (!TypeUtils.isValidType(formattedPrimaryType)) {
            return false;
        }
        if (formattedSecondaryType != null && !formattedSecondaryType.isEmpty() && !TypeUtils.isValidType(formattedSecondaryType)) {
            return false;
        }
        if (formattedSecondaryType != null && formattedSecondaryType.equalsIgnoreCase(formattedPrimaryType)) {
            return false;
        }

        Move newMove = new Move(formattedName, formattedDescription, classification, formattedPrimaryType, formattedSecondaryType);
        moveList.add(newMove);
        return true;
    }

    /**
     * Checks if a move name already exists in the list.
     *
     * @param name the move name to check
     * @return true if name is taken, false otherwise
     */
    public boolean isMoveNameTaken(String name) {
        for (Move m : moveList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Move> getAllMoves() {
        return new ArrayList<>(moveList);
    }

    public List<String> getAllMoveInfoStrings() {
        List<String> output = new ArrayList<>();

        for (Move m : moveList) {
            output.add(m.displayInfo());
        }
        return output;
    }

    public List<Move> searchByNameOrEffect(String keyword) {
        List<Move> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Move m : moveList) {
            if (m.getName().toLowerCase().contains(lowerKeyword) ||
                    m.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(m);
            }
        }
        return matches;
    }

    public List<Move> searchByType(String typeName) {
        List<Move> results = new ArrayList<>();
        String lowerTypeName = typeName.toLowerCase();
        for (Move m : moveList) {
            boolean matchesPrimary = m.getPrimaryType().equalsIgnoreCase(lowerTypeName);
            boolean matchesSecondary = m.getSecondaryType() != null && m.getSecondaryType().equalsIgnoreCase(lowerTypeName);
            if (matchesPrimary || matchesSecondary) {
                results.add(m);
            }
        }
        return results;
    }

    /**
     * Searches for moves by classification (HM or TM).
     *
     * @param classification The classification to search (HM or TM).
     * @return A List of Move objects that match the search criteria.
     */
    public List<Move> searchByClassification(Move.Classification classification) {
        List<Move> results = new ArrayList<>();
        for (Move m : moveList) {
            if (m.getClassification() == classification) {
                results.add(m);
            }
        }
        return results;
    }


    /**
     * Loads two default sample moves when the program starts.
     */
    public void loadDefaultMoves() {
        Move tackle = new Move("Tackle",
                "Tackle is one of the most common and basic moves a Pokémon learns. It deals damage with no additional effects.",
                Move.Classification.TM, "Normal", "");
        Move defend = new Move("Defend", "Raises user's defense stat temporarily.",
                Move.Classification.TM, "Normal", "");

        moveList.add(tackle);
        moveList.add(defend);
    }

    public Move findMove(String moveName) {
        for (Move m : moveList) {
            if (m.getName().equalsIgnoreCase(moveName)) {
                return m;
            }
        }
        return null;
    }
}
