package combat;

import characters.Character;
import characters.Hero;
import characters.Monster;
import characters.Party;
import world.Tile;
import world.World;
import world.ValorWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the execution of combat actions.
 */
public class CombatExecutor {
    private final World world;
    private final Party party;
    private final List<Monster> monsters;

    public CombatExecutor(World world, Party party, List<Monster> monsters) {
        this.world = world;
        this.party = party;
        this.monsters = monsters;
    }

    /**
     * Execute an attack action.
     */
    public boolean executeAttack(Character attacker, Character defender) {
        AttackAction action = new AttackAction(world);
        boolean success = action.execute(attacker, defender);
        
        if (defender.isFainted()) {
            handleDefeat(attacker, defender);
        }
        
        return success;
    }

    /**
     * Execute a spell action.
     */
    public boolean executeSpell(Character attacker, Character defender, items.Spell spell) {
        SpellAction action = new SpellAction(spell, world);
        boolean success = action.execute(attacker, defender);
        
        if (defender.isFainted()) {
            handleDefeat(attacker, defender);
        }
        
        return success;
    }

    /**
     * Get characters in range of a given character.
     */
    public List<Character> getCharactersInRange(Character character, int range) {
        List<Character> inRange = new ArrayList<>();
        
        int charRow = character.getRow();
        int charCol = character.getCol();
        
        // Check all heroes
        if (party != null) {
            for (Hero hero : party.getHeroes()) {
                if (hero.isFainted()) continue;
                if (hero == character) continue;
                
                int distance = Math.max(
                    Math.abs(hero.getRow() - charRow),
                    Math.abs(hero.getCol() - charCol)
                );
                
                if (distance <= range) {
                    inRange.add(hero);
                }
            }
        }
        
        // Check all monsters
        if (monsters != null) {
            for (Monster monster : monsters) {
                if (monster.isFainted()) continue;
                if (monster == character) continue;
                
                int distance = Math.max(
                    Math.abs(monster.getRow() - charRow),
                    Math.abs(monster.getCol() - charCol)
                );
                
                if (distance <= range) {
                    inRange.add(monster);
                }
            }
        }
        
        return inRange;
    }

    /**
     * Handle what happens when a character is defeated.
     */
    private void handleDefeat(Character attacker, Character defeated) {
        if (defeated instanceof Monster) {
            handleMonsterDefeat(attacker, (Monster) defeated);
        } else if (defeated instanceof Hero) {
            handleHeroDefeat((Hero) defeated);
        }
    }

    /**
     * Handle monster defeat - give rewards to hero.
     */
    private void handleMonsterDefeat(Character attacker, Monster monster) {
        System.out.println(monster.getName() + " has been defeated!");

        // Only give rewards if attacker is a hero
        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            
            // Award gold and experience
            int goldReward = monster.getLevel() * 100;
            int expReward = monster.getLevel() * 2;

            hero.addGold(goldReward);
            hero.addExperience(expReward);

            System.out.println(hero.getName() + " gained " + goldReward + " gold and " + expReward + " XP!");
        }

        // Remove monster from tile
        if (world != null) {
            Tile monsterTile = world.getTile(monster.getRow(), monster.getCol());
            if (monsterTile != null) {
                monsterTile.removeMonster();
            }
        }

        // Remove from monsters list
        if (monsters != null) {
            monsters.remove(monster);
        }
    }

    /**
     * Handle hero defeat - respawn at nexus for Valor, or game over for M&H.
     */
    private void handleHeroDefeat(Hero hero) {
        System.out.println(hero.getName() + " has been defeated!");

        // Check if this is Legends of Valor (respawn mechanic)
        if (world instanceof ValorWorld) {
            handleValorHeroDefeat(hero);
        } else {
            // Monsters & Heroes - hero stays defeated
            System.out.println(hero.getName() + " is out of the battle!");
            
            // Check if all heroes are defeated
            if (party != null) {
                boolean allDefeated = true;
                for (Hero h : party.getHeroes()) {
                    if (h.isAlive()) {
                        allDefeated = false;
                        break;
                    }
                }

                if (allDefeated) {
                    System.out.println("\n=== GAME OVER ===");
                    System.out.println("All heroes have been defeated!");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Handle hero defeat in Legends of Valor - respawn at nexus.
     */
    private void handleValorHeroDefeat(Hero hero) {
        System.out.println(hero.getName() + " will respawn at their Nexus.");

        // Remove hero from current tile
        if (world != null) {
            Tile currentTile = world.getTile(hero.getRow(), hero.getCol());
            if (currentTile != null) {
                currentTile.removeHero();
            }
        }

        // Revive hero (clears fainted flag and sets HP/MP to half)
        hero.revive();

        // Get hero's lane and respawn at bottom nexus
        int laneIndex = hero.getLaneIndex();
        int[] laneCols = {0, 3, 6};
        int respawnRow = ((ValorWorld) world).getSize() - 1; // Bottom row
        int respawnCol = laneCols[laneIndex];

        // Place hero at nexus
        if (world != null) {
            Tile nexusTile = world.getTile(respawnRow, respawnCol);
            if (nexusTile != null && nexusTile.isAccessible()) {
                nexusTile.setHero(hero, laneIndex + 1);
                hero.setPosition(respawnRow, respawnCol);
                System.out.println(hero.getName() + " respawned at Lane " + (laneIndex + 1) + 
                    " Nexus with 50% HP/MP.");
            } else {
                System.out.println("ERROR: Could not respawn " + hero.getName() + 
                    " - nexus tile not available!");
            }
        }
    }

    /**
     * Get the world instance.
     */
    public World getWorld() {
        return world;
    }
}