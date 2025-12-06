package util;

import java.util.Scanner;

/**
 * Inputer handler class.
 */
public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Read a string from user input.
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Read an integer from user input with validation.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Read a single character from user input.
     */
    public static char readChar(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.length() > 0 ? input.charAt(0) : '\0';
    }

    /**
     * Close the scanner.
     */
    public static void close() {
        scanner.close();
    }
}
