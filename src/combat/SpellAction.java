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

        // Check mana
        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana! Need " + spell.getManaCost() + 
                " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        // Deduct mana
        hero.setMana(hero.getMana() - spell.getManaCost());

        // Check dodge
        double dodgeChance = defender instanceof Monster ? 
            ((Monster) defender).getDodgeChance() : 0.0;
            
        if (random.nextDouble() < dodgeChance) {
            System.out.println(defender.getName() + " evades the magical assault!");
            hero.getInventory().removeItem(spell);
            return true;
        }

        // Calculate spell damage with dexterity bonus
        int effectiveDex = getEffectiveDexterity(hero);
        int damage = (int) (spell.getDamage() + (effectiveDex / 10000.0) * spell.getDamage());

        // Apply damage
        defender.takeDamage(damage);
        System.out.println(hero.getName() + " casts " + spell.getName() + " on " +
            defender.getName() + " for " + damage + " damage!");

        // Apply spell effect if target is a monster
        if (defender instanceof Monster) {
            ((Monster) defender).applySpellEffect(spell.getSpellType().toString());
        }

        // Remove spell from inventory
        hero.getInventory().removeItem(spell);

        // Check if defender was defeated
        if (defender.isFainted()) {
            System.out.println(defender.getName() + " has been slain!");
        }

        return true;
    }

    @Override
    public String getActionName() {
        return "Cast " + spell.getName();
    }

    /**
     * Get effective dexterity with terrain bonuses (Bush: +10%).
     */
    private int getEffectiveDexterity(Hero hero) {
        if (world == null) {
            return hero.getDexterity();
        }
        
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int dex = hero.getDexterity();
        if (tile != null && tile.getType() == TileType.BUSH) {
            dex = (int) (dex * (1 + TERRAIN_BONUS));
        }
        return dex;
    }
}