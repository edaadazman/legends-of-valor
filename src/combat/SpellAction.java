package combat;

import characters.Character;
import characters.Hero;
import characters.Monster;
import items.Spell;
import world.World;
import world.Tile;
import world.TileType;

import java.util.Random;

/**
 * Magical spell attack action.
 * Only heroes can cast spells.
 */
public class SpellAction implements CombatAction {
    private static final double TERRAIN_BONUS = 0.10;
    private Random random;
    private World world;
    private Spell spell;

    public SpellAction(Spell spell, World world) {
        this.spell = spell;
        this.world = world;
        this.random = new Random();
    }

    @Override
    public boolean execute(Character attacker, Character defender) {
        if (!(attacker instanceof Hero)) {
            System.out.println("Only heroes can cast spells!");
            return false;
        }

        Hero hero = (Hero) attacker;

        // Get character IDs
        String heroId = getCharacterId(hero);
        String defenderId = getCharacterId(defender);

        // Check mana
        if (hero.getMana() < spell.getManaCost()) {
            System.out.println(heroId + ": Insufficient mana! Need " + spell.getManaCost() + 
                " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        // Check dodge
        double dodgeChance = defender instanceof Monster ? 
            ((Monster) defender).getDodgeChance() : 0.0;
            
        if (random.nextDouble() < dodgeChance) {
            System.out.println(defenderId + ": " + defender.getName() + 
                " evades " + heroId + ": " + hero.getName() + "'s magical assault!");
            hero.getInventory().removeItem(spell);
            return true;
        }

        // Calculate spell damage with dexterity bonus
        int dex = hero.getDexterity();
        int damage = (int) (spell.getDamage() + (dex / 10000.0) * spell.getDamage());

        // Apply damage
        defender.takeDamage(damage);
        System.out.println(heroId + ": " + hero.getName() + " casts " + spell.getName() + 
            " on " + defenderId + ": " + defender.getName() + " for " + damage + " damage!");

        // Apply spell effect if target is a monster
        if (defender instanceof Monster) {
            ((Monster) defender).applySpellEffect(spell.getSpellType().toString());
        }

        // Remove spell from inventory
        hero.getInventory().removeItem(spell);

        // Check if defender was defeated
        if (defender.isFainted()) {
            System.out.println(defenderId + ": " + defender.getName() + " has been slain!");
        }

        return true;
    }

    /**
     * Get character ID string (H1, H2, M1, M2, etc.)
     */
    private String getCharacterId(Character character) {
        if (world == null) {
            return "";
        }
        
        Tile tile = world.getTile(character.getRow(), character.getCol());
        if (tile == null) {
            return "";
        }
        
        if (character instanceof Hero) {
            int id = tile.getHeroId();
            return id > 0 ? "H" + id : "H";
        } else if (character instanceof Monster) {
            int id = tile.getMonsterId();
            return id > 0 ? "M" + id : "M";
        }
        
        return "";
    }
    
    @Override
    public String getActionName() {
        return "Cast " + spell.getName();
    }
}