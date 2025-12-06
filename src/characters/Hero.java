package characters;

import items.*;

/**
 * Class for a Hero character in the game.
 */
public class Hero extends Character {
    private HeroType heroType;
    private int mana;
    private int maxMana;
    private int strength;
    private int dexterity;
    private int agility;
    private int gold;
    private int experience;
    private Inventory inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public Hero(String name, int level, HeroType heroType, int mana,
            int strength, int dexterity, int agility, int gold) {
        super(name, level, level * 100);
        this.heroType = heroType;
        this.mana = mana;
        this.maxMana = mana;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.experience = 0;
        this.inventory = new Inventory();
        this.equippedWeapon = null;
        this.equippedArmor = null;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, mana);
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void equipWeapon(Weapon weapon) {
        if (equippedWeapon != null) {
            inventory.addItem(equippedWeapon);
        }
        this.equippedWeapon = weapon;
    }

    public void unequipWeapon() {
        if (equippedWeapon != null) {
            inventory.addItem(equippedWeapon);
            equippedWeapon = null;
        }
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public void equipArmor(Armor armor) {
        if (equippedArmor != null) {
            inventory.addItem(equippedArmor);
        }
        this.equippedArmor = armor;
    }

    public void unequipArmor() {
        if (equippedArmor != null) {
            inventory.addItem(equippedArmor);
            equippedArmor = null;
        }
    }

    /**
     * Calculate the hero's attack damage with equipped weapon.
     */
    public int calculateAttackDamage() {
        int weaponDamage = equippedWeapon != null ? equippedWeapon.getDamage() : 0;
        return (int) ((strength + weaponDamage) * 0.05);
    }

    /**
     * Calculate the hero's dodge chance based on agility.
     */
    public double getDodgeChance() {
        return agility * 0.0002; // Reduced from 0.002 to balance gameplay
    }

    /**
     * Calculate damage reduction from equipped armor.
     */
    public int getDamageReduction() {
        return equippedArmor != null ? equippedArmor.getDamageReduction() : 0;
    }

    /**
     * Recover HP and Mana at end of battle round (10% each).
     */
    public void recover() {
        this.hp = (int) (this.hp * 1.1);
        this.mana = (int) (this.mana * 1.1);
    }

    /**
     * Revive a fainted hero with half HP and mana.
     */
    public void revive() {
        this.hp = this.maxHp / 2;
        this.mana = this.maxMana / 2;
    }

    /**
     * Check if hero has enough experience to level up.
     */
    private void checkLevelUp() {
        int expNeeded = level * 10;
        if (experience >= expNeeded) {
            levelUp();
        }
    }

    /**
     * Level up the hero, increasing stats.
     */
    private void levelUp() {
        level++;
        experience = 0;

        this.maxHp = level * 100;
        this.hp = this.maxHp;

        this.maxMana = (int) (maxMana * 1.1);
        this.mana = this.maxMana;

        strength = (int) (strength * 1.05);
        dexterity = (int) (dexterity * 1.05);
        agility = (int) (agility * 1.05);

        switch (heroType) {
            case WARRIOR:
                strength = (int) (strength * 1.05);
                agility = (int) (agility * 1.05);
                break;
            case SORCERER:
                dexterity = (int) (dexterity * 1.05);
                agility = (int) (agility * 1.05);
                break;
            case PALADIN:
                strength = (int) (strength * 1.05);
                dexterity = (int) (dexterity * 1.05);
                break;
        }

        System.out.println("\n" + name + " has reached level " + level + "!");
        System.out.println("Stats have increased! You feel more powerful...");
    }

    @Override
    public void displayStats() {
        System.out.println("=== " + name + " ===");
        System.out.println("Type: " + heroType);
        System.out.println("Level: " + level + " | XP: " + experience + "/" + (level * 10));
        System.out.println("HP: " + hp + "/" + maxHp + " | MP: " + mana + "/" + maxMana);
        System.out.println("Strength: " + strength + " | Dexterity: " + dexterity + " | Agility: " + agility);
        System.out.println("Gold: " + gold);
        System.out.println("Weapon: " + (equippedWeapon != null ? equippedWeapon.getName() : "None"));
        System.out.println("Armor: " + (equippedArmor != null ? equippedArmor.getName() : "None"));
    }

    @Override
    public String toString() {
        return name + " | HP: " + hp + " | MP: " + mana + " | Level: " + level;
    }
}
