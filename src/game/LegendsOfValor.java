package game;

import characters.Hero;
import characters.Monster;
import data.GameDatabase;
import items.Spell;
import util.InputHelper;
import util.AsciiArt;
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
    private int roundCounter;
    private Difficulty difficulty;

    public LegendsOfValor() {
        super();
        this.monsters = new ArrayList<>();
        this.roundCounter = 0;
        this.difficulty = Difficulty.EASY;
    }

    @Override
    protected void displayWelcome() {
        AsciiArt.displayLegendsOfValorIntro();
    }

    /**
     * Display difficulty selection menu.
     */
    private void selectDifficulty() {
        System.out.println("\n" + "===========================================================");
        System.out.println("  SELECT DIFFICULTY");
        System.out.println("===========================================================");
        
        Difficulty[] difficulties = Difficulty.values();
        for (int i = 0; i < difficulties.length; i++) {
            System.out.println((i + 1) + ") " + difficulties[i].name() + 
                " - " + difficulties[i].getDescription());
        }
        System.out.println("===========================================================");

        int choice = InputHelper.readInt("Choose difficulty (1-3): ", 1, difficulties.length);
        this.difficulty = difficulties[choice - 1];

        System.out.println("\nDifficulty set to: " + difficulty.name());
        System.out.println(difficulty.getDescription());
        System.out.println();
    }

    @Override
    protected int getRequiredHeroCount() {
        return 3; // Valor always uses exactly 3 heroes
    }

    @Override
    public void start() {
        displayWelcome();
        selectDifficulty();;
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
            // Display round number at the start of each round
            System.out.println("\n" + "===========================================================");
            System.out.println("  ROUND " + (roundCounter + 1));
            System.out.println("===========================================================");

            world.display();

            if (battleEngine.checkHeroVictory()) {
                gameRunning = false;
                break;
            }

            if (battleEngine.checkMonsterVictory()) {
                gameRunning = false;
                break;
            }

            int heroesToPlay = Math.min(3, party.size());
            for (int heroIdx = 0; heroIdx < heroesToPlay && gameRunning; heroIdx++) {
                Hero hero = party.getHero(heroIdx);

                boolean turnComplete = false;
                while (!turnComplete && gameRunning) {
                    System.out.println();
                    System.out.println("\n--- H" + (heroIdx + 1) + " Turn (" + hero.getName() + ") ---");
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
                        case 'v':
                            // Use inherited inventory management from RPG
                            turnComplete = manageInventory(hero);
                            break;
                        case 'm':
                            attemptMarket(hero);
                            world.display();
                            break;
                        case 'i':
                            displayBattleInfo();
                            world.display();
                            break;
                        case 'h':
                            displayHelpScreen();
                            world.display();
                            break;
                        case 'p':
                            // Pass turn - hero does nothing
                            turnComplete = passTurn(hero);
                            break;
                        case 'q':
                            gameRunning = false;
                            break;
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                }
                
                if (gameRunning && turnComplete && heroIdx < heroesToPlay - 1) {
                    System.out.println();
                    world.display();
                }
            }

            // After all heroes move, monsters move
            if (gameRunning) {
                moveMonsters();
            }

            // End of round - heroes recover HP and Mana
            if (gameRunning) {
                recoverHeroes();
            }

            // Increment round counter and check for monster spawning
            if (gameRunning) {
                roundCounter++;
                if (roundCounter % difficulty.getSpawnInterval() == 0) {
                    spawnNewMonsters();
                }
            }
        }
    }

    /**
     * Display comprehensive battle information for all heroes and monsters.
     */
    private void displayBattleInfo() {
        System.out.println("\n" + "===========================================================");
        System.out.println("  BATTLE STATUS");
        System.out.println("===========================================================");

        // Display all heroes
        System.out.println("\n=== YOUR HEROES ===");
        for (int i = 0; i < party.size(); i++) {
            Hero hero = party.getHero(i);
            System.out.print("[H" + (i + 1) + "] ");
            hero.displayStats();
            System.out.println();
        }

        // Display all monsters
        System.out.println("\n=== ENEMY MONSTERS ===");
        if (monsters.isEmpty()) {
            System.out.println("No monsters remain on the battlefield!");
        } else {
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                System.out.print("[M" + (i + 1) + "] ");
                monster.displayStats();
                System.out.println();
            }
        }

        System.out.println("===========================================================");
    }

    /**
     * Display comprehensive help screen with game instructions.
     */
    private void displayHelpScreen() {
        System.out.println("\n" + "===========================================================================");
        System.out.println("                    LEGENDS OF VALOR - HOW TO PLAY");
        System.out.println("===========================================================================");
        
        System.out.println("\n OBJECTIVE:");
        System.out.println("  • Heroes: Reach the Monster Nexus (top row) to win");
        System.out.println("  • Monsters: Prevent heroes from reaching their Nexus");
        System.out.println("  • If a monster reaches the Hero Nexus (bottom row), you lose!");
        
        System.out.println("\n MAP LAYOUT:");
        System.out.println("  • 8x8 grid divided into 3 vertical lanes");
        System.out.println("  • Columns 2 and 5 are inaccessible barriers (X)");
        System.out.println("  • Row 0: Monster Nexus (N) - Hero victory condition");
        System.out.println("  • Row 7: Hero Nexus (N) - Monster victory condition");
        
        System.out.println("\n CONTROLS:");
        System.out.println("  W/A/S/D  - Move (North/West/South/East)");
        System.out.println("  F        - Attack adjacent monster");
        System.out.println("  C        - Cast spell on adjacent monster");
        System.out.println("  T        - Teleport to another lane");
        System.out.println("  R        - Recall to your spawn Nexus");
        System.out.println("  O        - Remove adjacent obstacle");
        System.out.println("  V        - Manage inventory (equip/use items)");
        System.out.println("  M        - Market (only at Nexus tiles)");
        System.out.println("  I        - Display battle information");
        System.out.println("  H        - Help (this screen)");
        System.out.println("  P        - Pass turn");
        System.out.println("  Q        - Quit game");
        
        System.out.println("\n  TERRAIN TYPES:");
        System.out.println("  N - Nexus       (Spawn zones, market access)");
        System.out.println("  P - Plain       (No special effect)");
        System.out.println("  B - Bush        (+10% Dexterity → better spell damage)");
        System.out.println("  C - Cave        (+10% Agility → better dodge chance)");
        System.out.println("  K - Koulou      (+10% Strength → better physical damage)");
        System.out.println("  O - Obstacle    (Blocks movement, can be removed)");
        System.out.println("  X - Inaccessible (Permanent lane barriers)");
        
        System.out.println("\n  COMBAT RULES:");
        System.out.println("  • Heroes can only attack/cast spells on adjacent monsters (range 1)");
        System.out.println("  • Heroes CANNOT move north past monsters in their lane");
        System.out.println("  • Monsters move south automatically toward Hero Nexus");
        System.out.println("  • Monsters CANNOT move past heroes in their lane");
        System.out.println("  • Defeated heroes respawn at their Nexus with full HP/MP");
        System.out.println("  • Defeated monsters are removed and drop gold/XP");
        
        System.out.println("\n STRATEGY TIPS:");
        System.out.println("  • Use terrain strategically (Bush for spells, Cave for dodging)");
        System.out.println("  • Teleport between lanes to support teammates");
        System.out.println("  • Shop at Nexus tiles to buy better equipment");
        System.out.println("  • Remove obstacles to create clear paths");
        System.out.println("  • Balance offense (pushing forward) with defense (blocking monsters)");
        System.out.println("  • Heroes recover 10% HP/MP at end of each round");
        
        System.out.println("\n  DIFFICULTY LEVELS:");
        System.out.println("  • EASY:   Monsters spawn every 8 rounds");
        System.out.println("  • MEDIUM: Monsters spawn every 6 rounds");
        System.out.println("  • HARD:   Monsters spawn every 4 rounds");
        System.out.println("  Current: " + difficulty.name() + " - Next spawn in " + 
            (difficulty.getSpawnInterval() - (roundCounter % difficulty.getSpawnInterval())) + 
            " rounds");
        
        System.out.println("\n MARKET ACCESS:");
        System.out.println("  • Available only at Nexus tiles (N)");
        System.out.println("  • Buy weapons, armor, potions, and spells");
        System.out.println("  • Sell unwanted items for gold");
        System.out.println("  • Check required levels before purchasing");
        
        System.out.println("\n INVENTORY MANAGEMENT:");
        System.out.println("  • Equipping weapons/armor CONSUMES your turn");
        System.out.println("  • Using potions CONSUMES your turn");
        System.out.println("  • Viewing inventory does NOT consume turn");
        System.out.println("  • Spells are single-use and removed after casting");
        
        System.out.println("\n" + "===========================================================================");
        System.out.println("Press ENTER to return to game...");
        System.out.println("===========================================================================");
        
        InputHelper.readChar("");
    }


    private void displayControls(Hero hero) {
        System.out.println("W/A/S/D - Move");
        System.out.println("F - Attack monster");
        System.out.println("C - Cast spell");
        System.out.println("T - Teleport to another lane");
        System.out.println("R - Recall to Nexus");
        System.out.println("O - Remove adjacent obstacle");
        System.out.println("V - Inventory Actions (equipment/potions)");
        // Only show Market option if hero is at a Nexus
        if (isHeroAtNexus(hero)) {
            System.out.println("M - Market (buy/sell items)");
        }
        
        System.out.println("I - Info");
        System.out.println("H - How To Play");
        System.out.println("P - Pass turn");
        System.out.println("Q - Quit");
    }

    /**
     * Spawn 3 new monsters (one per lane) at the top nexus.
     * Monsters are leveled to match the highest level hero.
     * Called based on diffculty setting.
     */
    private void spawnNewMonsters() {
        System.out.println("\n" + "===========================================================================");
        System.out.println("    REINFORCEMENTS ARRIVING! ");
        System.out.println("  Enemy forces are spawning at the Monster Nexus!");
        System.out.println("===========================================================================");

        GameDatabase db = GameDatabase.getInstance();
        int highestHeroLevel = party.getHighestLevel();
        
        int[] laneCols = {1, 4, 7}; // Monster spawn columns
        int row = 0; // Top nexus row

        int spawned = 0;
        for (int i = 0; i < 3; i++) {
            Tile tile = world.getTile(row, laneCols[i]);
            
            // Only spawn if the tile is empty (no monster already there)
            if (tile != null && !tile.hasMonster() && tile.isAccessible()) {
                // Get random monster template
                Monster template = db.getRandomMonster();
                if (template == null) {
                    System.out.println("Could not create monster for lane " + (i + 1) + ".");
                    continue;
                }

                // Create monster at hero level
                Monster monster = new Monster(
                    template.getName(),
                    highestHeroLevel,
                    template.getMonsterType(),
                    template.getBaseDamage(),
                    template.getDefense(),
                    (int) (template.getDodgeChance() * 100)
                );

                // Find next available monster ID
                int monsterId = monsters.size() + 1;

                // Place monster on tile
                tile.setMonster(monster, monsterId);
                monster.setPosition(row, laneCols[i]);
                monster.setLaneIndex(i);
                monsters.add(monster);

                System.out.println("M" + monsterId + ": " + monster.getName() + 
                    " (Level " + monster.getLevel() + " " + monster.getMonsterType() + 
                    ") spawned in lane " + (i + 1) + "!");
                spawned++;
            } else {
                System.out.println("Lane " + (i + 1) + " spawn blocked - monster already present!");
            }
        }

        if (spawned > 0) {
            System.out.println("\n" + spawned + " new monster(s) have joined the battle!");
            System.out.println("Defend your nexus!\n");
        } else {
            System.out.println("\nAll spawn points are blocked! No new monsters spawned.\n");
        }
    }

    /**
     * Heroes recover HP and Mana at the end of each round.
     */
    private void recoverHeroes() {
        System.out.println("\n--- End of Round Recovery ---");
        
        boolean anyRecovery = false;
        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive()) {
                int oldHp = hero.getHp();
                int oldMana = hero.getMana();
                
                hero.recover();
                
                int hpGained = hero.getHp() - oldHp;
                int manaGained = hero.getMana() - oldMana;
                
                if (hpGained > 0 || manaGained > 0) {
                    System.out.println(hero.getName() + " recovers:");
                    if (hpGained > 0) {
                        System.out.println("  HP: +" + hpGained + " (" + hero.getHp() + "/" + hero.getMaxHp() + ")");
                    }
                    if (manaGained > 0) {
                        System.out.println("  MP: +" + manaGained + " (" + hero.getMana() + "/" + hero.getMaxMana() + ")");
                    }
                    anyRecovery = true;
                }
            }
        }
        
        if (!anyRecovery) {
            System.out.println("All heroes are at full health and mana!");
        }
    }

    /**
     * Check if hero is currently at a Nexus tile.
     */
    private boolean isHeroAtNexus(Hero hero) {
        Tile currentTile = world.getTile(hero.getRow(), hero.getCol());
        return currentTile != null && currentTile.getType() == TileType.NEXUS;
    }

    private boolean passTurn(Hero hero) {
        // Get hero ID
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int heroId = tile != null ? tile.getHeroId() : 0;
        
        System.out.println("H" + heroId + ": " + hero.getName() + 
            " observes the battlefield and waits...");
        return true; // Turn consumed
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

        System.out.println("\n" + "===========================================================================");
        System.out.println("  NEXUS MARKET");
        System.out.println("  Welcome, " + hero.getName() + "!");
        System.out.println("  Trading Post at the Nexus");
        System.out.println("===========================================================================");
        
        marketEngine.enterMarketForHero(hero);
    }

    private boolean attemptMove(Hero hero, int dr, int dc) {
        // Get hero ID before moving
        Tile oldTile = world.getTile(hero.getRow(), hero.getCol());
        int heroId = oldTile != null ? oldTile.getHeroId() : 0;
        
        // If trying to move forward (up), check if we're trying to move PAST a monster in our lane
        if (dr == -1 && dc == 0) { // Moving up (forward)
            // Determine which lane the hero is in
            int laneStartCol = (hero.getCol() / 3) * 3; // 0, 3, or 6
            int laneEndCol = laneStartCol + 1;
            
            // Check if there's a monster at the same row in another column of this lane
            for (int checkCol = laneStartCol; checkCol <= laneEndCol; checkCol++) {
                if (checkCol == hero.getCol()) continue;
                
                Tile sameLevelTile = world.getTile(hero.getRow(), checkCol);
                if (sameLevelTile != null && sameLevelTile.hasMonster()) {
                    Monster monster = sameLevelTile.getMonster();
                    int monsterId = sameLevelTile.getMonsterId();
                    System.out.println("H" + heroId + ": Cannot move past M" + monsterId + 
                        " (" + monster.getName() + ") in your lane!");
                    System.out.println("You must engage it first.");
                    return false;
                }
            }
        }
        
        boolean ok = world.moveHero(hero, dr, dc);
        if (!ok) {
            System.out.println("H" + heroId + ": Move blocked.");
        } else {
            // Check if hero reached the top nexus (row 0)
            if (hero.getRow() == 0) {
                world.display();
                System.out.println("\n" + "===========================================================================");
                System.out.println(" VICTORY! H" + heroId + ": " + hero.getName() + 
                    " has reached the enemy nexus!");
                System.out.println("The heroes have won the battle!");
                System.out.println("============================================================================\n");
                gameRunning = false;
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
     * Hero attacks a monster using list-based target selection.
     * Displays all monsters in range with ID, Name, HP, and Level.
     */
    private boolean attemptAttack(Hero hero) {
        List<Monster> monstersInRange = battleEngine.getMonstersInRange(hero);
        
        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in range to attack!");
            System.out.println("Move adjacent to an enemy first.");
            return false;
        }

        // Display available targets
        System.out.println("\n=== Monsters in Range ===");
        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster m = monstersInRange.get(i);
            
            // Get monster ID from tile
            Tile tile = world.getTile(m.getRow(), m.getCol());
            int monsterId = tile != null ? tile.getMonsterId() : 0;
            
            System.out.println((i + 1) + ") M" + monsterId + ": " + m.getName() + 
                " | HP: " + m.getHp() + 
                " | Level: " + m.getLevel() +
                " | Position: (" + m.getRow() + "," + m.getCol() + ")");
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Select target: ", 0, monstersInRange.size());
        if (choice == 0) {
            return false;
        }

        Monster target = monstersInRange.get(choice - 1);
        return battleEngine.heroAttack(hero, target);
    }

    /**
     * Hero casts spell on a monster using list-based target selection.
     * Player selects spell first, then target from list.
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

        System.out.println("\n=== Available Spells ===");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println((i + 1) + ") " + s.getName() + 
                " | Damage: " + s.getDamage() +
                " | Mana: " + s.getManaCost() + 
                " | Type: " + s.getSpellType());
        }
        System.out.println("0) Cancel");

        int spellChoice = InputHelper.readInt("Choose spell: ", 0, spells.size());
        if (spellChoice == 0) {
            return false;
        }

        Spell spell = spells.get(spellChoice - 1);

        // Check mana before showing targets
        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana! Need " + spell.getManaCost() + 
                " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        // Display available targets
        System.out.println("\n=== Monsters in Range ===");
        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster m = monstersInRange.get(i);

            Tile tile = world.getTile(m.getRow(), m.getCol());
            int monsterId = tile != null ? tile.getMonsterId() : 0;
            
            System.out.println((i + 1) + ") M" + monsterId + ": " + m.getName() + 
                " | HP: " + m.getHp() + 
                " | Level: " + m.getLevel() +
                " | Position: (" + m.getRow() + "," + m.getCol() + ")");
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Select target: ", 0, monstersInRange.size());
        if (choice == 0) {
            return false;
        }

        Monster target = monstersInRange.get(choice - 1);
        return battleEngine.heroCastSpell(hero, spell, target);
    }

    /** Recall hero back to their spawn nexus. */
    private boolean attemptRecall(Hero hero, int heroIdx) {
        int currentRow = hero.getRow();
        int currentCol = hero.getCol();
        int spawnRow = hero.getSpawnRow();
        int spawnCol = hero.getSpawnCol();

        if (currentRow == spawnRow && currentCol == spawnCol) {
            System.out.println(hero.getName() + " is already at their nexus.");
            return false;
        }

        Tile spawnTile = world.getTile(spawnRow, spawnCol);
        if (spawnTile == null) {
            System.out.println("Cannot recall: spawn location is invalid.");
            return false;
        }

        if (spawnTile.hasHero()) {
            System.out.println("Cannot recall: spawn nexus is occupied by another hero.");
            return false;
        }

        Tile currentTile = world.getTile(currentRow, currentCol);
        if (currentTile != null) {
            currentTile.removeHero();
        }

        // Place hero at spawn nexus
        spawnTile.setHero(hero, heroIdx + 1);
        hero.setPosition(spawnRow, spawnCol);

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