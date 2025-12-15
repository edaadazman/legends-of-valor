package combat;

import characters.Character;
import characters.Hero;
import characters.Monster;
import world.World;
import world.Tile;
import world.TileType;

import java.util.Random;

/**
 * Basic physical attack action.
 * Can be used by both heroes and monsters with terrain bonuses.
 */
public class AttackAction implements CombatAction {
    private static final double TERRAIN_BONUS = 0.10;
    private Random random;
    private World world;

    public AttackAction(World world) {
        this.random = new Random();
        this.world = world;
    }

    @Override
    public boolean execute(Character attacker, Character defender) {
        // Check dodge
        if (random.nextDouble() < getEffectiveDodgeChance(defender)) {
            System.out.println(defender.getName() + " dodged " + attacker.getName() + "'s attack!");
            return true;
        }

        // Calculate damage
        int damage = calculateDamage(attacker);
        damage = Math.max(1, damage - getDefense(defender));

        // Apply damage
        defender.takeDamage(damage);
        System.out.println(attacker.getName() + " attacks " + defender.getName() + 
            " for " + damage + " damage!");

        return true;
    }

    @Override
    public String getActionName() {
        return "Attack";
    }

    /**
     * Calculate attack damage based on character type and terrain.
     */
    private int calculateDamage(Character attacker) {
        if (attacker instanceof Hero) {
            return calculateHeroDamage((Hero) attacker);
        } else if (attacker instanceof Monster) {
            return calculateMonsterDamage((Monster) attacker);
        }
        return 0;
    }

    /**
     * Calculate hero attack damage with terrain bonuses.
     */
    private int calculateHeroDamage(Hero hero) {
        int weaponDamage = hero.getEquippedWeapon() != null ? 
            hero.getEquippedWeapon().getDamage() : 0;
        return (int) ((hero.getStrength() + weaponDamage) * 0.05);
    }

    /**
     * Calculate monster attack damage.
     */
    private int calculateMonsterDamage(Monster monster) {
        return monster.calculateAttackDamage();
    }

    /**
     * Get effective dodge chance with terrain bonuses.
     */
    private double getEffectiveDodgeChance(Character character) {
        if (character instanceof Hero) {
            Hero hero = (Hero) character;
            // Agility already includes terrain buff
            return hero.getAgility() * 0.0002;
        } else if (character instanceof Monster) {
            return ((Monster) character).getDodgeChance();
        }
        return 0.0;
    }

    /**
     * Get character's defense value.
     */
    private int getDefense(Character character) {
        if (character instanceof Hero) {
            return ((Hero) character).getDamageReduction();
        } else if (character instanceof Monster) {
            return ((Monster) character).getDefense();
        }
        return 0;
    }
}