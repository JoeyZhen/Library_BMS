package request;

import system.CSV;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a collection of arguments.
 *
 * @author Zachary Cook
 */
public class Arguments implements Serializable {
    private int pointer;
    private List<String> parameters;

    /**
     * Creates the arguments collection.
     *
     * @param arguments the pre-split arguments.
     */
    public Arguments(ArrayList<String> arguments) {
        this.pointer = 0;
        this.parameters = arguments;
    }

    /**
     * Creates the arguments collection.
     *
     * @param arguments a string of comma-separated values for parameters
     *                  and ending with a semicolon.
     */
    public Arguments(String arguments) {
        // Throw an error if the last character isn't a semicolon.
        if (!arguments.subSequence(arguments.length() - 1,arguments.length()).equals(";")) {
            throw new IllegalArgumentException("Last character must be a semicolon.");
        }

        // Remove the last character.
        arguments = arguments.substring(0,arguments.length() - 1);

        // Store the parameters.
        this.pointer = 0;
        this.parameters = CSV.parseCSV(arguments);
    }

    /**
     * Creates an clone of the object starting at the current pointer.
     *
     * @return a new arguments parser.
     */
    public Arguments cloneFromCurrentPointer() {
        // Get the arguments to use.
        ArrayList<String> subArguments = new ArrayList<>();
        for (int i = this.pointer; i < this.parameters.size(); i++) {
            subArguments.add(this.parameters.get(i));
        }

        // Create the new argument parser.
        return new Arguments(subArguments);
    }

    /**
     * Offsets the pointer.
     *
     * @param offset the offset to apply.
     */
    public void offsetPointer(int offset) {
        // Determine the new pointer.
        int newPointer = this.pointer + offset;
        if (newPointer < 0) {
            newPointer = 0;
        }

        // Set the pointer.
        this.pointer = newPointer;
    }

    /**
     * Resets the pointer to the beginning.
     */
    public void resetPointer() {
        this.pointer = 0;
    }

    /**
     * Returns if there is another string.
     *
     * @return if there is another string.
     */
    public boolean hasNext() {
        return this.pointer < this.parameters.size();
    }

    /**
     * Returns the next string.
     *
     * @return the next string.
     */
    public String getNextString() {
        // Return null if there is no next String.
        if (!this.hasNext()) {
            return null;
        }

        // Increment the pointer and return the next string.
        this.pointer += 1;
        return this.parameters.get(this.pointer - 1);
    }

    /**
     * Returns the next list as a set of strings.
     *
     * @return the next string as a list.
     */
    public List<String> getNextListAsStrings() {
        // Get the next string.
        String nextString = this.getNextString();

        // If the next string doesn't exist, return null.
        if (nextString == null) {
            return null;
        }

        // Parse and return the list as a string.
        return CSV.parseCSV(nextString);
    }

    /**
     * Returns the remaining strings.
     *
     * @return the remaining strings.
     */
    public List<String> getRemainingStrings() {
        // Assemble the list.
        ArrayList<String> remainingStrings = new ArrayList<>();
        while (this.hasNext()) {
            remainingStrings.add(this.getNextString());
        }

        // Return the list.
        return remainingStrings;
    }

    /**
     * Returns the next integer, or null if there
     * is no next entry or the number can't be formatted.
     *
     * @return the next integer.
     */
    public Integer getNextInteger() {
        // Get the next string.
        String nextString = this.getNextString();

        // If the next string doesn't exist, return null.
        if (nextString == null) {
            return null;
        }

        // Try to parse the integer.
        try {
            return Integer.parseInt(nextString);
        } catch (NumberFormatException ignored) {

        }

        // Return null (failed).
        return null;
    }

    /**
     * Returns the next list as a set of integers. The list format
     * of the string must be {int1,int2,...}.
     *
     * @return the next string as a list of integers.
     */
    public List<Integer> getNextListAsIntegers() {
        // Create the list of strings and return null if it is null.
        List<String> nextStrings = this.getNextListAsStrings();
        if (nextStrings == null) {
            return null;
        }

        // Convert the strings to integers.
        ArrayList<Integer> integerList = new ArrayList<>();
        for (String string : nextStrings) {
            try {
                integerList.add(Integer.parseInt(string));
            } catch (NumberFormatException ignored) {
                integerList.add(null);
            }
        }

        // Return the list.
        return integerList;
    }

    /**
     * Returns the remaining integers.
     *
     * @return the remaining integers.
     */
    public List<Integer> getRemainingIntegers() {
        // Parse the list from strings to integers.
        List<String> remainingStrings = this.getRemainingStrings();
        ArrayList<Integer> remainingIntegers = new ArrayList<>();
        for (String string : remainingStrings) {
            try {
                remainingIntegers.add(Integer.parseInt(string));
            } catch (NumberFormatException ignored) {
                remainingIntegers.add(null);
            }
        }

        // Return the list.
        return remainingIntegers;
    }
}