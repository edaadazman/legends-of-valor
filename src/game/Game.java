package game;


/**
 * Abstract base class for all game types.
 */
public abstract class Game {
    protected boolean gameRunning;

    public Game() {
        this.gameRunning = true;
    }

    /**
     * Start and run the game.
     */
    public abstract void start();

    /**
     * Display welcome message for the game.
     */
    protected abstract void displayWelcome();

    /**
     * Main game loop.
     */
    protected abstract void gameLoop();

    /**
     * Handle cleanup when game ends.
     */
    protected abstract void endGame();

    public boolean isRunning() {
        return gameRunning;
    }

    public void stop() {
        gameRunning = false;
    }
}