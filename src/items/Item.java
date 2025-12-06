package items;

/**
 * Base class for all items in the game.
 */
public abstract class Item {
    protected String name;
    protected int price;
    protected int requiredLevel;
    protected int uses;
    protected int maxUses;

    public Item(String name, int price, int requiredLevel, int maxUses) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.maxUses = maxUses;
        this.uses = maxUses;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getUses() {
        return uses;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public boolean canUse() {
        return uses > 0;
    }

    public void use() {
        if (uses > 0) {
            uses--;
        }
    }

    public int getSellPrice() {
        return price / 2;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return name + " | Price: " + price + " | Required Level: " + requiredLevel;
    }
}

