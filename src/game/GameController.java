package game;

import util.AsciiArt;

/**
 * Main game controller that manages game selection and execution.
 * Uses GameFactory to create game instances.
 */
public class GameController {
    
    public void start() {
        AsciiArt.displayMainMenu();
        
        Game game = GameFactory.createGameFromUserInput();
        
        if (game != null) {
            game.start();
        } else {
            AsciiArt.displayExitMessage();
        }
    }
}