package characters;

/**
 * Base class for all characters (Heroes and Monsters).
 * Contains common attributes like name, level, and HP.
 */
public abstract class Character {
    protected String name;
    protected int level;
    protected int hp;
    protected int maxHp;

    public Character(String name, int level, int hp) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.maxHp = hp;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isFainted() {
        return hp <= 0;
    }

    public abstract void displayStats();
}
