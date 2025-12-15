package game;

import characters.Hero;
import characters.Monster;
import data.GameDatabase;
import items.Spell;
import util.InputHelper;
import world.Tile;
import world.TileType;
import world.ValorWorld;
import combat.ValorBattleEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Legends of Valor game implementation.
 */
public class LegendsOfValor extends RPG {
    private ValorWorld world;
    private ValorBattleEngine battleEngine;
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
        battleEngine = new ValorBattleEngine(world, party, monsters);

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

                boolean turnComplete = false;
                while (!turnComplete && gameRunning) {
                    displayControls(hero);
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
                        case 'f':
                            turnComplete = attemptAttack(hero);
                            break;
                        case 'c':
                            turnComplete = attemptCastSpell(hero);
                            break;
                        case 't':
                            turnComplete = attemptTeleport(hero, heroIdx);
                            break;
                        case 'r':
                            turnComplete = attemptRecall(hero, heroIdx);
                            break;
                        case 'o':
                            turnComplete = attemptRemoveObstacle(hero);
                            break;
                        case 'm':
                            attemptMarket(hero);
                            // Market doesn't consume turn
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
                // Display world after each hero's turn except last
                if (gameRunning && turnComplete && heroIdx < heroesToPlay - 1) {
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

    private void displayControls(Hero hero) {
        System.out.println("W/A/S/D - Move");
        System.out.println("F - Attack monster");
        System.out.println("C - Cast spell");
        System.out.println("T - Teleport to another lane");
        System.out.println("R - Recall to Nexus");
        System.out.println("O - Remove adjacent obstacle");
        // Only show Market option if hero is at a Nexus
        if (isHeroAtNexus(hero)) {
            System.out.println("M - Market (buy/sell items)");
        }
        
        System.out.println("I - Info");
        System.out.println("Q - Quit");
    }

    /**
     * Check if hero is currently at a Nexus tile.
     */
    private boolean isHeroAtNexus(Hero hero) {
        Tile currentTile = world.getTile(hero.getRow(), hero.getCol());
        return currentTile != null && currentTile.getType() == TileType.NEXUS;
    }

    /**
     * Attempt to access the market.
     * Only works if hero is at a Nexus tile.
     */
    private void attemptMarket(Hero hero) {
        if (!isHeroAtNexus(hero)) {
            System.out.println("You must be at a Nexus to access the market!");
            System.out.println("Use [R] to Recall to your Nexus, or reach the enemy Nexus.");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("  NEXUS MARKET");
        System.out.println("  Welcome, " + hero.getName() + "!");
        System.out.println("  Trading Post at the Nexus");
        System.out.println("=".repeat(50));
        
        marketEngine.enterMarket(party);
    }

    private boolean attemptMove(Hero hero, int dr, int dc) {
        
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

    /**
     * Parse directional input (N, E, S, W, NE, SE, SW, NW) into row/col offsets.
     * Returns int[2] with {deltaRow, deltaCol}, or null if invalid.
     */
    private int[] parseDirection(String input) {
        String dir = input.trim().toUpperCase();
        
        switch (dir) {
            case "N":
                return new int[]{-1, 0};  // North (up)
            case "E":
                return new int[]{0, 1};   // East (right)
            case "S":
                return new int[]{1, 0};   // South (down)
            case "W":
                return new int[]{0, -1};  // West (left)
            case "NE":
                return new int[]{-1, 1};  // Northeast (up-right)
            case "SE":
                return new int[]{1, 1};   // Southeast (down-right)
            case "SW":
                return new int[]{1, -1};  // Southwest (down-left)
            case "NW":
                return new int[]{-1, -1}; // Northwest (up-left)
            default:
                return null;
        }
    }

    /**
     * Hero attacks a monster using unified combat system.
     * Player selects target by 8-directional input (N, E, S, W, NE, SE, SW, NW).
     */
    private boolean attemptAttack(Hero hero) {
        List<Monster> monstersInRange = battleEngine.getMonstersInRange(hero);
        
        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in range to attack!");
            System.out.println("Move adjacent to an enemy first.");
            return false;
        }

        // Get direction from player
        String dirInput = InputHelper.readString("Attack direction (N/E/S/W/NE/SE/SW/NW): ");
        
        int[] delta = parseDirection(dirInput);
        if (delta == null) {
            System.out.println("Invalid attack direction.");
            return false;
        }

        int targetRow = hero.getRow() + delta[0];
        int targetCol = hero.getCol() + delta[1];

        // Validate target tile
        Tile targetTile = world.getTile(targetRow, targetCol);
        if (targetTile == null || !targetTile.hasMonster()) {
            System.out.println("No monster in that direction.");
            return false;
        }

        Monster monster = targetTile.getMonster();

        // Use unified combat system (AttackAction with terrain bonuses)
        return battleEngine.heroAttack(hero, monster);
    }

    /**
     * Hero casts spell on a monster using unified combat system.
     * Player selects spell, then target by 8-directional input (N, E, S, W, NE, SE, SW, NW).
     */
    private boolean attemptCastSpell(Hero hero) {
        List<Monster> monstersInRange = battleEngine.getMonstersInRange(hero);
        
        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in range to cast spells on!");
            System.out.println("Move adjacent to an enemy first.");
            return false;
        }

        // First, select spell
        List<Spell> spells = hero.getInventory().getSpells();
        if (spells.isEmpty()) {
            System.out.println(hero.getName() + " has no spells!");
            return false;
        }

        System.out.println("\nAvailable Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println((i + 1) + ") " + s.getName() + " | Damage: " + s.getDamage() +
                    " | Mana: " + s.getManaCost() + " | Type: " + s.getSpellType());
        }
        System.out.println("0) Cancel");

        int spellChoice = InputHelper.readInt("Choose spell: ", 0, spells.size());
        if (spellChoice == 0) {
            return false;
        }

        Spell spell = spells.get(spellChoice - 1);

        // Check mana before asking for direction
        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana! Need " + spell.getManaCost() + 
                " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        // Then get direction for target
        String dirInput = InputHelper.readString("Attack direction (N/E/S/W/NE/SE/SW/NW): ");
        
        int[] delta = parseDirection(dirInput);
        if (delta == null) {
            System.out.println("Invalid target direction.");
            return false;
        }

        int targetRow = hero.getRow() + delta[0];
        int targetCol = hero.getCol() + delta[1];

        // Validate target tile
        Tile targetTile = world.getTile(targetRow, targetCol);
        if (targetTile == null || !targetTile.hasMonster()) {
            System.out.println("No monster in that direction.");
            return false;
        }

        Monster monster = targetTile.getMonster();

        // Use unified combat system (SpellAction with terrain bonuses)
        return battleEngine.heroCastSpell(hero, spell, monster);
    }

    /** Recall hero back to their spawn nexus. */
    private boolean attemptRecall(Hero hero, int heroIdx) {
        int currentRow = hero.getRow();
        int currentCol = hero.getCol();
        int spawnRow = hero.getSpawnRow();
        int spawnCol = hero.getSpawnCol();

        // Check if hero is already at their nexus
        if (currentRow == spawnRow && currentCol == spawnCol) {
            System.out.println(hero.getName() + " is already at their nexus.");
            return false;
        }

        // Check if spawn nexus is occupied
        Tile spawnTile = world.getTile(spawnRow, spawnCol);
        if (spawnTile == null) {
            System.out.println("Cannot recall: spawn location is invalid.");
            return false;
        }

        if (spawnTile.hasHero()) {
            System.out.println("Cannot recall: spawn nexus is occupied by another hero.");
            return false;
        }

        // Remove hero from current tile
        Tile currentTile = world.getTile(currentRow, currentCol);
        if (currentTile != null) {
            currentTile.removeHero();
        }

        // Place hero at spawn nexus
        spawnTile.setHero(hero, heroIdx + 1);
        hero.setPosition(spawnRow, spawnCol);

        // Apply terrain buff for nexus tile
        hero.applyTerrainBuff(spawnTile.getType());

        System.out.println(hero.getName() + " recalled to their nexus at (" + spawnRow + "," + spawnCol + ")!");
        return true; // Recall consumes turn
    }

    /** Teleport hero to a space near another hero in a different lane. */
    private boolean attemptTeleport(Hero hero, int heroIdx) {
        // Find heroes in other lanes
        List<Hero> otherHeroes = new ArrayList<>();
        for (int i = 0; i < party.size(); i++) {
            if (i != heroIdx) {
                Hero other = party.getHero(i);
                // Check if in different lane (lanes are col 0-1, 3-4, 6-7)
                int currentLane = hero.getCol() / 3;
                int otherLane = other.getCol() / 3;
                if (currentLane != otherLane) {
                    otherHeroes.add(other);
                }
            }
        }

        if (otherHeroes.isEmpty()) {
            System.out.println("No heroes in other lanes to teleport to.");
            return false;
        }

        // Display hero options
        System.out.println("\nSelect a hero to teleport near:");
        for (int i = 0; i < otherHeroes.size(); i++) {
            Hero other = otherHeroes.get(i);
            System.out.println((i + 1) + ". " + other.getName() + " at (" + other.getRow() + "," + other.getCol() + ")");
        }

        int choice = InputHelper.readInt("Choice (1-" + otherHeroes.size() + "): ", 1, otherHeroes.size());
        Hero targetHero = otherHeroes.get(choice - 1);

        // Find available adjacent positions (left, right, below only - not above/forward)
        int[][] directions = {{0, -1}, {0, 1}, {1, 0}}; // left, right, below
        String[] directionNames = {"Left", "Right", "Below"};
        List<int[]> validPositions = new ArrayList<>();
        List<String> validNames = new ArrayList<>();

        for (int i = 0; i < directions.length; i++) {
            int newRow = targetHero.getRow() + directions[i][0];
            int newCol = targetHero.getCol() + directions[i][1];
            Tile tile = world.getTile(newRow, newCol);
            if (tile != null && tile.isAccessible() && !tile.hasHero() && !tile.hasMonster()) {
                validPositions.add(new int[]{newRow, newCol});
                validNames.add(directionNames[i]);
            }
        }

        if (validPositions.isEmpty()) {
            System.out.println("No valid positions available near " + targetHero.getName() + ".");
            return false;
        }

        // Display position options
        System.out.println("\nSelect a position:");
        for (int i = 0; i < validPositions.size(); i++) {
            int[] pos = validPositions.get(i);
            System.out.println((i + 1) + ". " + validNames.get(i) + " of " + targetHero.getName() + " at (" + pos[0] + "," + pos[1] + ")");
        }

        int posChoice = InputHelper.readInt("Choice (1-" + validPositions.size() + "): ", 1, validPositions.size());
        int[] targetPos = validPositions.get(posChoice - 1);

        // Remove hero from current tile
        Tile currentTile = world.getTile(hero.getRow(), hero.getCol());
        if (currentTile != null) {
            currentTile.removeHero();
        }

        // Place hero at target position
        Tile targetTile = world.getTile(targetPos[0], targetPos[1]);
        targetTile.setHero(hero, heroIdx + 1);
        hero.setPosition(targetPos[0], targetPos[1]);

        // Apply terrain buff for new tile
        hero.applyTerrainBuff(targetTile.getType());

        System.out.println(hero.getName() + " teleported to (" + targetPos[0] + "," + targetPos[1] + ")!");
        return true; // Teleport consumes turn
    }

    /**
     * Hero attempts to remove an adjacent obstacle.
     * Uses 8-directional targeting (N, E, S, W, NE, SE, SW, NW).
     * Consumes a turn.
     */
    private boolean attemptRemoveObstacle(Hero hero) {
        System.out.println("\nSelect direction to remove obstacle:");
        String dirInput = InputHelper.readString("Direction (N/E/S/W/NE/SE/SW/NW): ");
        
        int[] delta = parseDirection(dirInput);
        if (delta == null) {
            System.out.println("Invalid direction.");
            return false;
        }

        int targetRow = hero.getRow() + delta[0];
        int targetCol = hero.getCol() + delta[1];

        // Validate target tile exists
        Tile targetTile = world.getTile(targetRow, targetCol);
        if (targetTile == null) {
            System.out.println("Invalid location.");
            return false;
        }

        // Check if it's an obstacle
        if (!targetTile.isObstacle()) {
            System.out.println("No obstacle in that direction.");
            System.out.println("Obstacles are marked with [O] on the map.");
            return false;
        }

        // Remove the obstacle
        System.out.println(hero.getName() + " clears the obstacle!");
        ((ValorWorld) world).removeObstacle(targetRow, targetCol);
        
        return true; // Consumes turn
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
            hero.setSpawnLocation(row, col, i); // Track spawn location for recall

            // Apply initial terrain buff (Nexus has no buff)
            hero.applyTerrainBuff(tile.getType());
        }
    }

    /** Place 3 monsters on the top nexus (one per lane), randomly selected from Dragons, Exoskeletons, and Spirits. */
    private void placeMonstersAtTopNexus() {
        GameDatabase db = GameDatabase.getInstance();
        
        // One monster per lane (cols 0/3/6 are lane anchors)
        int[] laneCols = { 1, 4, 7 };
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

    /**
     * Move all monsters using unified combat system.
     */
    private void moveMonsters() {
        System.out.println("\n--- Monster Turn ---");
        
        for (Monster monster : new ArrayList<>(monsters)) {
            if (monster.isFainted()) {
                continue;
            }

            // Use battle engine for monster actions (attack or move using unified combat)
            battleEngine.executeMonsterTurn(monster);
        }
    }
}