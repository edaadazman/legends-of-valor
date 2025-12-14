package game;

/**
 * Main game controller that manages game selection and execution.
 * Uses GameFactory to create game instances.
 */
public class GameController {
    
    public void start() {
        displayWelcomeBanner();
        
        Game game = GameFactory.createGameFromUserInput();
        
        if (game != null) {
            game.start();
        } else {
            displayExitMessage();
        }
    }
    
    /**
     * Display the main welcome banner.
     */
    private void displayWelcomeBanner() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  LEGENDS GAME COLLECTION");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Display exit message when user quits.
     */
    private void displayExitMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  Thanks for playing!");
        System.out.println("  Goodbye!");
        System.out.println("=".repeat(50) + "\n");
    }
}