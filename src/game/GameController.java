package game;

import util.InputHelper;

/**
 * Main game controller that manages game selection and execution.
 */
public class GameController {
    
    public void start() {
        System.out.println("\n===========================================");
        System.out.println("  LEGENDS GAME COLLECTION");
        System.out.println("===========================================\n");

        Game game = selectGame();
        if (game != null) {
            game.start();
        }
    }

    private Game selectGame() {
        System.out.println("Select Game Mode:");
        System.out.println("1) Monsters and Heroes (classic)");
        System.out.println("2) Legends of Valor (new)");
        System.out.println("0) Exit");

        int choice = InputHelper.readInt("Enter choice: ", 0, 2);

        switch (choice) {
            case 1:
                return new MonstersAndHeroes();
            case 2:
                return new LegendsOfValor();
            case 0:
                System.out.println("Thanks for visiting! Goodbye!");
                return null;
            default:
                System.out.println("Invalid choice.");
                return null;
        }
    }
}