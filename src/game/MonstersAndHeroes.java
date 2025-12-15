package game;

import characters.Hero;
import combat.MHBattleEngine;
import items.*;
import util.InputHelper;
import world.*;
import java.util.List;
import java.util.Random;

/**
 * Monsters and Heroes game implementation.
 */
public class MonstersAndHeroes extends RPG {
    private static final double BATTLE_CHANCE = 0.2;
    
    private MHBattleEngine battleEngine;
    private MHWorld world;
    private Random random;

    public MonstersAndHeroes() {
        super();
        this.random = new Random();
    }

    @Override
    protected void displayWelcome() {
        System.out.println("\n===========================================");
        System.out.println("  LEGENDS: MONSTERS AND HEROES");
        System.out.println("  A Tale of Courage, Magic, and Glory");
        System.out.println("===========================================");
        System.out.println("\nThe realm is under siege by fearsome monsters...");
        System.out.println("Brave heroes are needed to restore peace to the land!\n");
    }

    @Override
    protected int getRequiredHeroCount() {
        return InputHelper.readInt("\nHow many heroes will join your quest? (1-3): ", 1, 3);
    }

    @Override
    public void start() {
        displayWelcome();
        setupParty();
        world = new MHWorld();
        battleEngine = new MHBattleEngine(world, party);

        System.out.println("\nYour epic journey begins!");
        System.out.println("May fortune favor the bold...\n");

        gameLoop();
        endGame();
    }

    @Override
    protected void gameLoop() {
        while (gameRunning) {
            displayWorld();
            displayControls();
            handleInput();
        }
    }

    private void displayWorld() {
        System.out.println();
        world.display();
    }

    private void displayControls() {
        System.out.println("\nControls:");
        System.out.println("W/A/S/D - Move");
        System.out.println("M - Market");
        System.out.println("I - Info");
        System.out.println("V - Inventory");
        System.out.println("Q - Quit");
        System.out.println();
    }

    private void handleInput() {
        char input = InputHelper.readChar("Enter command: ");
    
        switch (Character.toLowerCase(input)) {
            case 'w':
                moveParty(-1, 0);
                break;
            case 's':
                moveParty(1, 0);
                break;
            case 'a':
                moveParty(0, -1);
                break;
            case 'd':
                moveParty(0, 1);
                break;
            case 'm':
                enterMarket();
                break;
            case 'i':
                displayInfo();
                break;
            case 'v':
                // Use inherited inventory management from RPG
                selectHeroForInventory();
                break;
            case 'q':
                gameRunning = false;
                break;
            default:
                System.out.println("Invalid command!");
                break;
        }
    }

    /**
     * Select a hero and manage their inventory.
     * Uses inherited methods from RPG base class.
     */
    private void selectHeroForInventory() {
        System.out.println("\n=== Inventory Management ===");
        System.out.println("Choose hero:");
        for (int i = 0; i < party.size(); i++) {
            System.out.println((i + 1) + ") " + party.getHero(i).getName());
        }
        System.out.println("0) Back");

        int heroChoice = InputHelper.readInt("", 0, party.size());
        if (heroChoice == 0) {
            return;
        }

        Hero hero = party.getHero(heroChoice - 1);

        boolean inInventory = true;
        while (inInventory) {
            // Use inherited manageInventory method
            // In M&H, inventory actions don't consume turns
            manageInventory(hero);
            
            // Ask if they want to continue managing inventory
            String continueChoice = InputHelper.readString("\nContinue managing inventory? (y/n): ");
            if (!continueChoice.equalsIgnoreCase("y")) {
                inInventory = false;
            }
        }
    }

    private void moveParty(int deltaRow, int deltaCol) {
        if (world.moveHero(null, deltaRow, deltaCol)) {
            handleTileEvent();
        }
    }

    private void handleTileEvent() {
        Tile currentTile = world.getCurrentTile();

        if (currentTile.isMarket()) {
            System.out.println("\nYou've discovered a bustling marketplace!");
            System.out.println("Press [M] to browse wares and trade goods.");
        } else if (currentTile.isCommon()) {
            if (random.nextDouble() < BATTLE_CHANCE) {
                boolean won = battleEngine.startBattle(party);
                if (!won) {
                    gameRunning = false;
                }
            } else {
                System.out.println("\nThe path ahead is quiet. Your party continues onward.");
            }
        }
    }

    private void enterMarket() {
        Tile currentTile = world.getCurrentTile();

        if (!currentTile.isMarket()) {
            System.out.println("\nThere's no market here. Look for a [M] tile on the map!");
            return;
        }

        marketEngine.enterMarket(party);
    }
}