package combat;

import characters.Character;
import characters.Hero;
import characters.Monster;
import characters.Party;
import items.Spell;
import world.Tile;
import world.TileType;
import world.ValorWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles combat mechanics for Legends of Valor.
 * Extends BattleEngine to reuse common combat logic.
 */
public class ValorBattleEngine extends BattleEngine {

    public ValorBattleEngine(ValorWorld world, Party party, List<Monster> monsters) {
        super(world, party, monsters);
    }

    /**
     * Get monsters within attack range of a hero.
     */
    public List<Monster> getMonstersInRange(Hero hero) {
        List<Character> characters = getCharactersInRange(hero, 1);
        List<Monster> monstersInRange = new ArrayList<>();
        
        for (Character c : characters) {
            if (c instanceof Monster) {
                monstersInRange.add((Monster) c);
            }
        }
        
        return monstersInRange;
    }

    /**
     * Get heroes within attack range of a monster.
     */
    public List<Hero> getHeroesInRange(Monster monster) {
        List<Character> characters = getCharactersInRange(monster, 1);
        List<Hero> heroesInRange = new ArrayList<>();
        
        for (Character c : characters) {
            if (c instanceof Hero) {
                heroesInRange.add((Hero) c);
            }
        }
        
        return heroesInRange;
    }

    /**
     * Hero attacks a monster - Valor implementation.
     */
    public boolean heroAttack(Hero hero, Monster target) {
        return super.heroAttack(hero, target);
    }

    /**
     * Hero casts a spell - Valor implementation.
     */
    public boolean heroCastSpell(Hero hero, Spell spell, Monster target) {
        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana! Need " + spell.getManaCost() + 
                " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        return super.heroCastSpell(hero, spell, target);
    }

    /**
 * Execute monster turn - prioritize advancing to Nexus over attacking.
 * Only attack if hero is directly blocking forward progress.
 */
public void executeMonsterTurn(Monster monster) {
    if (monster.isFainted()) {
        return;
    }

    // Get monster ID from its current tile
    Tile currentTile = world.getTile(monster.getRow(), monster.getCol());
    int monsterId = currentTile != null ? currentTile.getMonsterId() : 0;

    // First, try to move forward toward Hero Nexus
    boolean canMoveForward = canMonsterMoveForward(monster);
    
    if (canMoveForward) {
        // No hero blocking forward - advance toward Nexus
        boolean moved = ((ValorWorld)world).moveMonster(monster);
        if (moved) {
            System.out.println("M" + monsterId + ": " + monster.getName() + 
                " advances south toward the Hero Nexus!");
            
            // Check if monster reached hero Nexus (win condition)
            if (monster.getRow() == ((ValorWorld)world).getSize() - 1) {
                Tile tile = world.getTile(monster.getRow(), monster.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n⚠️  M" + monsterId + ": " + monster.getName() + 
                        " has breached the Hero Nexus!");
                }
            }
        } else {
            // Movement failed (obstacle?) - try to attack if heroes nearby
            attemptMonsterCombat(monster, monsterId);
        }
    } else {
        // Hero is blocking forward progress - must fight through them
        List<Hero> heroesInRange = getHeroesInRange(monster);
        
        if (!heroesInRange.isEmpty()) {
            // Filter to only heroes directly blocking forward movement
            List<Hero> blockingHeroes = new ArrayList<>();
            for (Hero hero : heroesInRange) {
                if (isHeroBlockingPath(monster, hero)) {
                    blockingHeroes.add(hero);
                }
            }
            
            if (!blockingHeroes.isEmpty()) {
                // Attack a blocking hero
                Hero target = blockingHeroes.get(random.nextInt(blockingHeroes.size()));
                
                // Get hero ID
                Tile heroTile = world.getTile(target.getRow(), target.getCol());
                int heroId = heroTile != null ? heroTile.getHeroId() : 0;
                
                System.out.println("M" + monsterId + ": " + monster.getName() + 
                    " must fight through H" + heroId + " (" + target.getName() + ")!");
                monsterAttack(monster, target);
            } else {
                // Heroes nearby but not blocking - try to move anyway
                boolean moved = ((ValorWorld)world).moveMonster(monster);
                if (!moved) {
                    // Can't move, attack anyway
                    attemptMonsterCombat(monster, monsterId);
                } else {
                    System.out.println("M" + monsterId + ": " + monster.getName() + " advances south.");
                }
            }
        } else {
            // No heroes in range but can't move forward - stuck
            System.out.println("M" + monsterId + ": " + monster.getName() + 
                " is stuck and cannot advance.");
        }
    }
}

/**
 * Attempt monster combat if movement failed.
 */
private void attemptMonsterCombat(Monster monster, int monsterId) {
    List<Hero> heroesInRange = getHeroesInRange(monster);
    if (!heroesInRange.isEmpty()) {
        Hero target = heroesInRange.get(random.nextInt(heroesInRange.size()));
        monsterAttack(monster, target);
    }
}

    /**
     * Check if monster can move forward without being blocked.
     * Returns true if the forward path is clear.
     */
    private boolean canMonsterMoveForward(Monster monster) {
        int nextRow = monster.getRow() + 1; // Moving south (toward hero nexus)
        int col = monster.getCol();
        
        // Check if next tile exists and is accessible
        Tile nextTile = world.getTile(nextRow, col);
        if (nextTile == null || (!nextTile.isAccessible() && !nextTile.isObstacle())) {
            return false;
        }
        
        // Check if hero is in the same row in the lane (blocking sideways)
        int laneIndex = col / 3;
        int laneCol1 = laneIndex * 3;
        int laneCol2 = laneIndex * 3 + 1;
        
        // Check both columns of the lane at current row
        Tile tile1 = world.getTile(monster.getRow(), laneCol1);
        Tile tile2 = world.getTile(monster.getRow(), laneCol2);
        
        if ((tile1 != null && tile1.hasHero()) || (tile2 != null && tile2.hasHero())) {
            return false; // Hero is blocking at current row level
        }
        
        return true;
    }

    /**
     * Check if a hero is directly blocking the monster's path forward.
     * Returns true if hero must be fought before advancing.
     */
    private boolean isHeroBlockingPath(Monster monster, Hero hero) {
        // Hero must be in same lane
        int monsterLane = monster.getCol() / 3;
        int heroLane = hero.getCol() / 3;
        
        if (monsterLane != heroLane) {
            return false;
        }
        
        // Hero must be at same row or directly ahead (next row)
        int rowDiff = hero.getRow() - monster.getRow();
        return rowDiff >= 0 && rowDiff <= 1;
    }

    /**
     * Check if a hero has reached the monster Nexus (win condition).
     */
    public boolean checkHeroVictory() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive() && hero.getRow() == 0) {
                Tile tile = world.getTile(hero.getRow(), hero.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n" + "=".repeat(70));
                    System.out.println("   VICTORY! ");
                    System.out.println("  " + hero.getName() + " has reached the Monster Nexus!");
                    System.out.println("  The heroes have won the battle!");
                    System.out.println("=".repeat(70));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if a monster has reached the hero Nexus (lose condition).
     */
    public boolean checkMonsterVictory() {
        int heroNexusRow = ((ValorWorld)world).getSize() - 1;
        
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster.getRow() == heroNexusRow) {
                Tile tile = world.getTile(monster.getRow(), monster.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n" + "=".repeat(70));
                    System.out.println("    DEFEAT! ");
                    System.out.println("  " + monster.getName() + " has reached the Hero Nexus!");
                    System.out.println("  The monsters have won. Game Over!");
                    System.out.println("=".repeat(70));
                    return true;
                }
            }
        }
        return false;
    }
}