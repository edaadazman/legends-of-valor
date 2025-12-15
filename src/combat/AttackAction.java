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
    private Random random;
    private World world;

    public AttackAction(World world) {
        this.random = new Random();
        this.world = world;
    }

    @Override
    public boolean execute(Character attacker, Character defender) {
        // Get character IDs from tiles
        String attackerId = getCharacterId(attacker);
        String defenderId = getCharacterId(defender);
        
        // Check dodge
        if (random.nextDouble() < getEffectiveDodgeChance(defender)) {
            System.out.println(defenderId + ": " + defender.getName() + " dodged " + 
                attackerId + ": " + attacker.getName() + "'s attack!");
            return true;
        }
    
        // Calculate damage
        int damage = calculateDamage(attacker);
        damage = Math.max(1, damage - getDefense(defender));
    
        // Apply damage
        defender.takeDamage(damage);
        System.out.println(attackerId + ": " + attacker.getName() + " attacks " + 
            defenderId + ": " + defender.getName() + " for " + damage + " damage!");
    
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