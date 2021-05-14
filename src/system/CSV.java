package system;

import java.util.ArrayList;

/**
 * Handles parsing CSV lines.
 *
 * @author Zachary Cook
 */
public class CSV {
    /**
     * Reads and parses a CSV line.
     *
     * @param csvLine the line to parse.
     * @return the parsed CSV line as a CSV.
     */
    public static ArrayList<String> parseCSV(String csvLine) {
        ArrayList<String> parsedLine = new ArrayList<>();
        String currentString = "";
        boolean inBreak = false;

        // Add the lines.
        for (int i = 0; i < csvLine.length(); i++) {
            // Get the character.
            Character character = csvLine.charAt(i);

            // Interpret the character.
            if (inBreak) {
                if (character == '}' || character == '\"') {
                    inBreak = false;
                } else {
                    currentString += character;
                }
            } else {
                if (character == '{' || character == '\"') {
                    inBreak = true;
                } else if (character == ',') {
                    parsedLine.add(currentString);
                    currentString = "";
                } else {
                    currentString += character;
                }
            }
        }

        // Push the last string.
        parsedLine.add(currentString);

        // Return the parse CSV lined.
        return parsedLine;
    }
}
