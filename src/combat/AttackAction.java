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

        // Check if defender was defeated
        if (defender.isFainted()) {
            System.out.println(defender.getName() + " has been defeated!");
        }

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
        int effectiveStrength = getEffectiveStrength(hero);
        int weaponDamage = hero.getEquippedWeapon() != null ? 
            hero.getEquippedWeapon().getDamage() : 0;
        return (int) ((effectiveStrength + weaponDamage) * 0.05);
    }

    /**
     * Calculate monster attack damage.
     */
    private int calculateMonsterDamage(Monster monster) {
        return monster.calculateAttackDamage();
    }

    /**
     * Get effective strength with terrain bonuses (Koulou: +10%).
     */
    private int getEffectiveStrength(Hero hero) {
        if (world == null) {
            return hero.getStrength();
        }
        
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int str = hero.getStrength();
        if (tile != null && tile.getType() == TileType.KOULOU) {
            str = (int) (str * (1 + TERRAIN_BONUS));
        }
        return str;
    }

    /**
     * Get effective dodge chance with terrain bonuses.
     */
    private double getEffectiveDodgeChance(Character character) {
        if (character instanceof Hero) {
            Hero hero = (Hero) character;
            int effectiveAgility = getEffectiveAgility(hero);
            return effectiveAgility * 0.0002;
        } else if (character instanceof Monster) {
            return ((Monster) character).getDodgeChance();
        }
        return 0.0;
    }

    /**
     * Get effective agility with terrain bonuses (Cave: +10%).
     */
    private int getEffectiveAgility(Hero hero) {
        if (world == null) {
            return hero.getAgility();
        }
        
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int agi = hero.getAgility();
        if (tile != null && tile.getType() == TileType.CAVE) {
            agi = (int) (agi * (1 + TERRAIN_BONUS));
        }
        return agi;
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