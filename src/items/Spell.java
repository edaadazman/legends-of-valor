package items;

import characters.Hero;

/**
 * Class for a spell.
 */
public class Spell extends Item {
    private int damage;
    private int manaCost;
    private SpellType spellType;

    public Spell(String name, int price, int requiredLevel, int damage, int manaCost, SpellType spellType) {
        super(name, price, requiredLevel, 1);
        this.damage = damage;
        this.manaCost = manaCost;
        this.spellType = spellType;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    /**
     * Calculate spell damage based on hero's dexterity.
     */
    public int calculateDamage(Hero hero) {
        return (int) (damage + (hero.getDexterity() / 10000.0) * damage);
    }

    @Override
    public String getType() {
        return "Spell";
    }

    @Override
    public String toString() {
        return name + " | Damage: " + damage + " | Mana cost: " + manaCost;
    }
}
