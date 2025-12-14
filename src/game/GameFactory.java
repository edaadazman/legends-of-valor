package game;

import util.InputHelper;

/**
 * Factory for creating different game types.
 * Implements the Factory design pattern to encapsulate game instantiation logic.
 */
public class GameFactory {
    
    /**
     * Enum representing available game types with display information.
     */
    public enum GameType {
        MONSTERS_AND_HEROES("Monsters and Heroes", "Classic dungeon crawler with turn-based battles"),
        LEGENDS_OF_VALOR("Legends of Valor", "Strategic lane-based MOBA-style gameplay");
        
        private final String displayName;
        private final String description;
        
        GameType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Create a game instance based on the specified type.
     * 
     * @param type The type of game to create
     * @return A new game instance
     * @throws IllegalArgumentException if the game type is unknown
     */
    public static Game createGame(GameType type) {
        switch (type) {
            case MONSTERS_AND_HEROES:
                return new MonstersAndHeroes();
            case LEGENDS_OF_VALOR:
                return new LegendsOfValor();
            default:
                throw new IllegalArgumentException("Unknown game type: " + type);
        }
    }
    
    /**
     * Create a game by its ordinal index.
     * Useful for menu-based selection.
     * 
     * @param index The index of the game type (0-based)
     * @return A new game instance
     * @throws IllegalArgumentException if index is out of bounds
     */
    public static Game createGameByIndex(int index) {
        GameType[] types = GameType.values();
        if (index < 0 || index >= types.length) {
            throw new IllegalArgumentException("Invalid game index: " + index);
        }
        return createGame(types[index]);
    }
    
    /**
     * Prompt user to select a game type and create it interactively.
     * 
     * @return A new game instance, or null if user cancels
     */
    public static Game createGameFromUserInput() {
        displayGameMenu();
        
        GameType[] types = GameType.values();
        int choice = InputHelper.readInt("Enter choice: ", 0, types.length);
        
        if (choice == 0) {
            return null; // User chose to exit
        }
        
        return createGame(types[choice - 1]);
    }
    
    /**
     * Display the game selection menu.
     */
    private static void displayGameMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  SELECT GAME MODE");
        System.out.println("=".repeat(50));
        
        GameType[] types = GameType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ") " + types[i].getDisplayName());
            System.out.println("   " + types[i].getDescription());
        }
        System.out.println("0) Exit");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Get all available game types.
     * 
     * @return Array of all game types
     */
    public static GameType[] getAvailableGameTypes() {
        return GameType.values();
    }
    
    /**
     * Check if a game type is valid.
     * 
     * @param typeName The name of the game type to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidGameType(String typeName) {
        try {
            GameType.valueOf(typeName.toUpperCase().replace(" ", "_"));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Get a game type by its display name.
     * 
     * @param displayName The display name of the game
     * @return The corresponding GameType, or null if not found
     */
    public static GameType getGameTypeByDisplayName(String displayName) {
        for (GameType type : GameType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }
}