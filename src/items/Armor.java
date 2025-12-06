package items;

/**
 * Class for armor that heroes can equip to reduce damage.
 */
public class Armor extends Item {
    private int damageReduction;

    public Armor(String name, int price, int requiredLevel, int damageReduction) {
        super(name, price, requiredLevel, 10);
        this.damageReduction = damageReduction;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String getType() {
        return "Armor";
    }

    @Override
    public String toString() {
        return name + " | Damage Reduction: " + damageReduction;
    }
}
