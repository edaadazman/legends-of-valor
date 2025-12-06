package items;

/**
 * Class for a weapon item.
 */
public class Weapon extends Item {
    private int damage;
    private int handsRequired;

    public Weapon(String name, int price, int requiredLevel, int damage, int handsRequired) {
        super(name, price, requiredLevel, 10);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public int getDamage() {
        return damage;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    @Override
    public String getType() {
        return "Weapon";
    }

    @Override
    public String toString() {
        return name + " | Damage: " + damage + " | Hands: " + handsRequired;
    }
}
