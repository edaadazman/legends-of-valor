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
                        case 't':
                            // Attack - ask for direction
                            char attackDir = Character.toLowerCase(InputHelper.readChar("Attack direction (W/A/S/D): "));
                            turnComplete = attemptAttack(hero, attackDir);
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
                // Display world after each hero's turn
                if (gameRunning && turnComplete) {
                    System.out.println(); // Add blank line for readability
                    world.display();
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
        System.out.println("T - Choose Direction Then Attack monster");
        System.out.println("I - Info");
        System.out.println("Q - Quit");
    }

    private boolean attemptMove(Hero hero, int dr, int dc) {
        int newRow = hero.getRow() + dr;
        int newCol = hero.getCol() + dc;
        
        // Check if there's a monster at the target location - auto-attack it
        Tile targetTile = world.getTile(newRow, newCol);
        if (targetTile != null && targetTile.hasMonster()) {
            Monster monster = targetTile.getMonster();
            System.out.println(hero.getName() + " attacked " + monster.getName() + "!");
            System.out.println("TODO: Implement combat system");
            System.out.println("Monster was defeated!");
            
            // Remove monster from board and list
            targetTile.removeMonster();
            monsters.remove(monster);
            
            return true; // Combat ends turn
        }
        
        // If trying to move forward (up), check if we're trying to move PAST a monster in our lane
        // Lanes are: 0-1, 3-4, 6-7 (2-column wide)
        if (dr == -1 && dc == 0) { // Moving up (forward)
            // Determine which lane the hero is in
            int laneStartCol = (hero.getCol() / 3) * 3; // 0, 3, or 6
            int laneEndCol = laneStartCol + 1;
            
            // Check if there's a monster at the same row in another column of this lane
            // (trying to sidestep past a monster)
            for (int checkCol = laneStartCol; checkCol <= laneEndCol; checkCol++) {
                if (checkCol == hero.getCol()) continue; // Skip our own column
                
                Tile sameLevelTile = world.getTile(hero.getRow(), checkCol);
                if (sameLevelTile != null && sameLevelTile.hasMonster()) {
                    System.out.println("Cannot move past monster " + sameLevelTile.getMonster().getName() + " in your lane!");
                    System.out.println("You must engage it first.");
                    return false;
                }
            }
        }
        
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

    /** Attack a monster in an adjacent space (up, down, left, right). */
    private boolean attemptAttack(Hero hero, char direction) {
        int targetRow = hero.getRow();
        int targetCol = hero.getCol();

        // Determine target based on direction
        switch (direction) {
            case 'w': targetRow--; break; // up
            case 's': targetRow++; break; // down
            case 'a': targetCol--; break; // left
            case 'd': targetCol++; break; // right
            default:
                System.out.println("Invalid attack direction.");
                return false;
        }

        Tile targetTile = world.getTile(targetRow, targetCol);
        if (targetTile == null || !targetTile.hasMonster()) {
            System.out.println("No monster in that direction.");
            return false;
        }

        Monster monster = targetTile.getMonster();
        System.out.println(hero.getName() + " attacked " + monster.getName() + "!");
        System.out.println("TODO: Implement combat system");
        System.out.println("Monster was defeated!");

        // Remove monster from board and list
        targetTile.removeMonster();
        monsters.remove(monster);

        return true; // Attack consumes turn
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
        
        List<Monster> toRemove = new ArrayList<>();
        
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            int oldRow = monster.getRow();
            int oldCol = monster.getCol();
            
            // Check if there's a hero in the next space (combat trigger)
            Tile nextTile = world.getTile(oldRow + 1, oldCol);
            if (nextTile != null && nextTile.hasHero()) {
                System.out.println("M" + (i + 1) + " (" + monster.getName() + ") engaged " + nextTile.getHero().getName() + " in combat!");
                System.out.println("TODO: Implement combat system");
                System.out.println("Monster was defeated!");
                
                // Remove monster from board
                Tile monsterTile = world.getTile(oldRow, oldCol);
                if (monsterTile != null) {
                    monsterTile.removeMonster();
                }
                toRemove.add(monster);
                continue;
            }
            
            // Check if there's a hero directly adjacent (one space ahead) blocking movement
            if (nextTile != null && nextTile.hasHero()) {
                System.out.println("M" + (i + 1) + " (" + monster.getName() + ") cannot move past hero in lane");
                continue;
            }
            
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
        
        // Remove defeated monsters from the list
        monsters.removeAll(toRemove);
    }
}