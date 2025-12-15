package data.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for loading data from files.
 * Implements Template Method pattern for file reading.
 */
public abstract class DataLoader<T> {
    protected static final String DATA_PATH = "Legends_Monsters_and_Heroes/";

    /**
     * Load data from a file.
     * Template method that defines the loading process.
     */
    public List<T> loadFromFile(String filename) {
        List<T> items = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String header = br.readLine(); // Skip header
            String line;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                T item = parseLine(line);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        
        return items;
    }

    /**
     * Parse a single line from the file.
     * Subclasses must implement this method.
     */
    protected abstract T parseLine(String line);

    /**
     * Split line by whitespace and handle errors.
     */
    protected String[] splitLine(String line, int expectedParts) {
        String[] parts = line.split("\\s+");
        if (parts.length < expectedParts) {
            System.out.println("Invalid line format: " + line);
            return null;
        }
        return parts;
    }

    /**
     * Safely parse an integer.
     */
    protected Integer parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Invalid " + fieldName + ": " + value);
            return null;
        }
    }
}