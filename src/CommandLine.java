import system.SerializedLibraryBookManagementSystem;

import java.util.Scanner;

/**
 * Provides a command line interface to the system.
 *
 * @author Zachay Cook
 */
public class CommandLine {
    /**
     * Main program for the system.
     */
    public static void main(String[] args) {
        // Create the system.
        SerializedLibraryBookManagementSystem system = SerializedLibraryBookManagementSystem.loadFromFile();

        // Print the initial statement.
        System.out.println("Welcome to the Library Book Management System.\nType in any command or exit; to close the program.\nAll changes will be saved.");

        // Run the command line loop.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Get the next input.
            String input = scanner.nextLine();

            // Complete the request.
            if (input.toLowerCase().equals("exit;")) {
                break;
            } else {
                System.out.println(system.performRequest(input));
            }
        }
    }
}
