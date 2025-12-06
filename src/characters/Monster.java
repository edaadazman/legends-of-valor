package characters;

/**
 * Class for a Monster character in the game.
 */
public class Monster extends Character {
    private MonsterType monsterType;
    private int baseDamage;
    private int defense;
    private double dodgeChance;

    public Monster(String name, int level, MonsterType monsterType,
            int baseDamage, int defense, int dodgeChance) {
        super(name, level, level * 100);
        this.monsterType = monsterType;
        this.baseDamage = baseDamage;
        this.defense = (int) (defense * 0.05);
        this.dodgeChance = dodgeChance * 0.001;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = Math.max(0, dodgeChance);
    }

    /**
     * Calculate attack damage for the monster.
     */
    public int calculateAttackDamage() {
        return (int) (baseDamage * 0.05);
    }

    /**
     * Apply spell effect to reduce monster's stat.
     */
    public void applySpellEffect(String effectType) {
        switch (effectType.toUpperCase()) {
            case "FIRE":
                defense = (int) (defense * 0.9);
                System.out.println("Flames weaken " + name + "'s defenses!");
                break;
            case "ICE":
                baseDamage = (int) (baseDamage * 0.9);
                System.out.println("Ice numbs " + name + ", reducing its power!");
                break;
            case "LIGHTNING":
                dodgeChance = dodgeChance * 0.9;
                System.out.println("Lightning strikes, slowing " + name + "!");
                break;
        }
    }

    @Override
    public void displayStats() {
        System.out.println(name + " | lvl: " + level + " | HP: " + hp +
                " | Damage: " + baseDamage + " | Defense: " + defense +
                " | Dodge: " + (int) (dodgeChance * 100));
    }

    @Override
    public String toString() {
        return name + " | HP: " + hp;
    }
}
