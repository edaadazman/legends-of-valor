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
     * Execute monster turn - attack if hero in range, otherwise move south.
     */
    public void executeMonsterTurn(Monster monster) {
        List<Hero> heroesInRange = getHeroesInRange(monster);

        if (!heroesInRange.isEmpty()) {
            Hero target = heroesInRange.get(random.nextInt(heroesInRange.size()));
            monsterAttack(monster, target);
        } else {
            boolean moved = ((ValorWorld)world).moveMonster(monster);
            if (moved) {
                System.out.println(monster.getName() + " advances south.");
            }
        }
    }

    /**
     * Check if a hero has reached the monster Nexus (win condition).
     */
    public boolean checkHeroVictory() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive() && hero.getRow() == 0) {
                Tile tile = world.getTile(hero.getRow(), hero.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n=== VICTORY! ===");
                    System.out.println(hero.getName() + " has reached the Monster Nexus!");
                    System.out.println("The heroes have won the battle!");
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
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster.getRow() == ((ValorWorld)world).getSize() - 1) {
                Tile tile = world.getTile(monster.getRow(), monster.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n=== DEFEAT! ===");
                    System.out.println(monster.getName() + " has reached the Hero Nexus!");
                    System.out.println("The monsters have won. Game Over!");
                    return true;
                }
            }
        }
        return false;
    }
}