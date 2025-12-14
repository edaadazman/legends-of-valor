package game;

import characters.Hero;
import characters.Monster;
import data.GameDatabase;
import util.InputHelper;
import world.Tile;
import world.ValorWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Legends of Valor game implementation.
 */
public class LegendsOfValor extends RPG {
    private ValorWorld world;
    private final List<Monster> monsters;
    private final Random random;

    public LegendsOfValor() {
        super();
        this.monsters = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    protected void displayWelcome() {
        System.out.println("\n===========================================");
        System.out.println("  LEGENDS OF VALOR");
        System.out.println("  Three lanes await—defend your nexus!");
        System.out.println("===========================================\n");
    }

    @Override
    protected int getRequiredHeroCount() {
        return 3; // Valor always uses exactly 3 heroes
    }

    @Override
    public void start() {
        displayWelcome();
        setupParty();
        world = new ValorWorld();
        placeHeroesAtBottomNexus();
        placeMonstersAtTopNexus();

        gameLoop();
        endGame();
    }

    @Override
    protected void gameLoop() {
        while (gameRunning) {
            world.display();

            // Heroes move in order H1 -> H3 each round
            int heroesToPlay = Math.min(3, party.size());
            for (int heroIdx = 0; heroIdx < heroesToPlay && gameRunning; heroIdx++) {
                Hero hero = party.getHero(heroIdx);

                System.out.println("\n--- H" + (heroIdx + 1) + " Turn (" + hero.getName() + ") ---");
                displayControls();

                boolean turnComplete = false;
                while (!turnComplete && gameRunning) {
                    char cmd = Character.toLowerCase(InputHelper.readChar("Command: "));
                    switch (cmd) {
                        case 'w':
                            turnComplete = attemptMove(hero, -1, 0);
                            break;
                        case 's':
                            turnComplete = attemptMove(hero, 1, 0);
                            break;
                        case 'a':
                            turnComplete = attemptMove(hero, 0, -1);
                            break;
                        case 'd':
                            turnComplete = attemptMove(hero, 0, 1);
                            break;
                        case 'i':
                            hero.displayStats();
                            break;
                        case 'q':
                            gameRunning = false;
                            break;
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                }
            }

            // After all heroes move, monsters move down
            if (gameRunning) {
                moveMonsters();
            }
        }
    }

    private void displayControls() {
        System.out.println("W/A/S/D - Move");
        System.out.println("I - Info");
        System.out.println("Q - Quit");
    }

    private boolean attemptMove(Hero hero, int dr, int dc) {
        boolean ok = world.moveHero(hero, dr, dc);
        if (!ok) {
            System.out.println("Move blocked.");
        } else {
            // Check if hero reached the top nexus (row 0)
            if (hero.getRow() == 0) {
                world.display();
                System.out.println("\n" + "=".repeat(50));
                System.out.println("VICTORY! " + hero.getName() + " has reached the enemy nexus!");
                System.out.println("The heroes have won the battle!");
                System.out.println("=".repeat(50) + "\n");
                System.exit(0);
            }
        }
        return ok;
    }

    private void placeHeroesAtBottomNexus() {
        if (party.size() == 0) {
            System.out.println("No heroes in party.");
            return;
        }

        int[] laneCols = { 0, 3, 6 };
        int row = world.getSize() - 1;

        int heroesToPlace = Math.min(Math.min(party.size(), laneCols.length), 3);
        for (int i = 0; i < heroesToPlace; i++) {
            Hero hero = party.getHero(i);
            int col = laneCols[i];

            Tile tile = world.getTile(row, col);
            if (tile == null || !tile.isAccessible() || tile.hasHero()) {
                System.out.println("Could not place " + hero.getName() + " at (" + row + "," + col + ").");
                continue;
            }

            tile.setHero(hero, i + 1);
            hero.setPosition(row, col);
        }
    }

    /** Place 3 monsters on the top nexus (one per lane), randomly selected from Dragons, Exoskeletons, and Spirits. */
    private void placeMonstersAtTopNexus() {
        GameDatabase db = GameDatabase.getInstance();
        
        // One monster per lane (cols 0/3/6 are lane anchors)
        int[] laneCols = { 0, 3, 6 };
        int row = 0; // top Nexus row

        for (int i = 0; i < 3; i++) {
            // Randomly pick a monster from the database
            Monster template = db.getRandomMonster();
            if (template == null) {
                System.out.println("Could not create monster M" + (i + 1) + ".");
                continue;
            }

            // Create a level 1 monster based on the template
            Monster monster = new Monster(
                template.getName(),
                1, // Starting level
                template.getMonsterType(),
                template.getBaseDamage(),
                template.getDefense(),
                (int) (template.getDodgeChance() * 100)
            );

            int col = laneCols[i];
            Tile tile = world.getTile(row, col);
            if (tile == null || !tile.isAccessible() || tile.hasMonster()) {
                System.out.println("Could not place M" + (i + 1) + " at (" + row + "," + col + ").");
                continue;
            }

            tile.setMonster(monster, i + 1);
            monster.setPosition(row, col);
            monsters.add(monster);
            
            System.out.println("M" + (i + 1) + ": " + monster.getName() + " (" + monster.getMonsterType() + ") spawned at lane " + (i + 1));
        }
    }

    /** Move all monsters down one row each turn and print their moves. */
    private void moveMonsters() {
        System.out.println("\n--- Monster Turn ---");
        
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            int oldRow = monster.getRow();
            int oldCol = monster.getCol();
            
            boolean moved = world.moveMonster(monster);
            
            if (moved) {
                System.out.println("M" + (i + 1) + " (" + monster.getName() + ") moved from (" + oldRow + "," + oldCol + ") to (" + monster.getRow() + "," + monster.getCol() + ")");
                
                // Check if monster reached the bottom nexus (last row)
                if (monster.getRow() == world.getSize() - 1) {
                    world.display();
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("DEFEAT! " + monster.getName() + " has reached your nexus!");
                    System.out.println("The monsters have won the battle!");
                    System.out.println("=".repeat(50) + "\n");
                    System.exit(0);
                }
            } else {
                System.out.println("M" + (i + 1) + " (" + monster.getName() + ") could not move (blocked)");
            }
        }
    }
}