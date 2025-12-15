package characters;

import items.*;
import world.TileType;

/**
 * Class for a Hero character in the game.
 */
public class Hero extends Character {

    private static final double TERRAIN_BONUS = 0.10;

    private HeroType heroType;
    private int mana;
    private int maxMana;
    private int baseStrength;
    private int baseDexterity;
    private int baseAgility;
    private int strength;
    private int dexterity;
    private int agility;
    private int gold;
    private int experience;
    private Inventory inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    // Spawn tracking for Legends of Valor
    private int spawnRow;
    private int spawnCol;
    private int laneIndex;

    // Current terrain buff
    private TileType currentTerrain;

    public Hero(String name, int level, HeroType heroType, int mana,
            int strength, int dexterity, int agility, int gold) {
        super(name, level, level * 100);
        this.heroType = heroType;
        this.mana = mana;
        this.maxMana = mana;
        this.baseStrength = strength;
        this.baseDexterity = dexterity;
        this.baseAgility = agility;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.experience = 0;
        this.inventory = new Inventory();
        this.equippedWeapon = null;
        this.equippedArmor = null;
        this.spawnRow = -1;
        this.spawnCol = -1;
        this.laneIndex = -1;
        this.currentTerrain = TileType.PLAIN;
    }

    /**
     * Apply terrain buff when hero enters a tile.
     */
    public void applyTerrainBuff(TileType terrain) {
        // Remove old buff first
        removeTerrainBuff();
        
        this.currentTerrain = terrain;
        
        switch (terrain) {
            case BUSH:
                // + dexterity
                this.dexterity = (int) (baseDexterity * (1 + TERRAIN_BONUS));
                System.out.println(name + " feels more nimble in the bushes! Dexterity increased.");
                break;
            case CAVE:
                // + agility
                this.agility = (int) (baseAgility * (1 + TERRAIN_BONUS));
                System.out.println(name + " feels more agile in the cave! Agility increased.");
                break;
            case KOULOU:
                // + strength
                this.strength = (int) (baseStrength * (1 + TERRAIN_BONUS));
                System.out.println(name + " feels stronger on the koulou! Strength increased.");
                break;
            default:
                // Plain or other tiles - no buff
                break;
        }
    }

    /**
     * Remove terrain buff when hero leaves a tile.
     */
    public void removeTerrainBuff() {
        if (currentTerrain != TileType.PLAIN && currentTerrain != TileType.NEXUS) {
            this.strength = baseStrength;
            this.dexterity = baseDexterity;
            this.agility = baseAgility;
        }
        this.currentTerrain = TileType.PLAIN;
    }

    /**
     * Update base stats when leveling up or using items.
     */
    private void updateBaseStats() {
        this.baseStrength = strength;
        this.baseDexterity = dexterity;
        this.baseAgility = agility;
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
        this.baseStrength = strength;
        this.strength = strength;
        applyTerrainBuff(currentTerrain); // Re-apply terrain buff
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.baseDexterity = dexterity;
        this.dexterity = dexterity;
        applyTerrainBuff(currentTerrain); // Re-apply terrain buff
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.baseAgility = agility;
        this.agility = agility;
        applyTerrainBuff(currentTerrain); // Re-apply terrain buff
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
        return agility * 0.0002;
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
        this.hp = Math.min(maxHp, hp + (int) (maxHp * 0.1));
        this.mana = Math.min(maxMana, mana + (int) (maxMana * 0.1));
    }

    /**
     * Revive a fainted hero with half HP and mana.
     */
    public void revive() {
        this.hp = this.maxHp / 2;
        this.mana = this.maxMana / 2;
    }

    /**
     * Respawn hero at their Nexus with full HP and MP (for Legends of Valor).
     */
    public void respawnAtNexus() {
        this.hp = this.maxHp;
        this.mana = this.maxMana;
        this.row = this.spawnRow;
        this.col = this.spawnCol;
    }

    // Spawn tracking getters/setters for Legends of Valor
    public int getSpawnRow() {
        return spawnRow;
    }

    public int getSpawnCol() {
        return spawnCol;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public void setSpawnLocation(int row, int col, int laneIndex) {
        this.spawnRow = row;
        this.spawnCol = col;
        this.laneIndex = laneIndex;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public TileType getCurrentTerrain() {
        return currentTerrain;
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

        // Update base stats
        baseStrength = (int) (baseStrength * 1.05);
        baseDexterity = (int) (baseDexterity * 1.05);
        baseAgility = (int) (baseAgility * 1.05);

        switch (heroType) {
            case WARRIOR:
                baseStrength = (int) (baseStrength * 1.05);
                baseAgility = (int) (baseAgility * 1.05);
                break;
            case SORCERER:
                baseDexterity = (int) (baseDexterity * 1.05);
                baseAgility = (int) (baseAgility * 1.05);
                break;
            case PALADIN:
                baseStrength = (int) (baseStrength * 1.05);
                baseDexterity = (int) (baseDexterity * 1.05);
                break;
        }

        // Re-apply terrain buff with new base stats
        applyTerrainBuff(currentTerrain);

        System.out.println("\n" + name + " has reached level " + level + "!");
        System.out.println("Stats have increased! You feel more powerful...");
    }

    @Override
    public void displayStats() {
        System.out.println("=== " + name + " ===");
        System.out.println("Type: " + heroType);
        System.out.println("Level: " + level + " | XP: " + experience + "/" + (level * 10));
        System.out.println("HP: " + hp + "/" + maxHp + " | MP: " + mana + "/" + maxMana);
        
        // Show base stats and current (buffed) stats
        String strDisplay = strength != baseStrength ? 
            baseStrength + " → " + strength + " ↑" : String.valueOf(strength);
        String dexDisplay = dexterity != baseDexterity ? 
            baseDexterity + " → " + dexterity + " ↑" : String.valueOf(dexterity);
        String agiDisplay = agility != baseAgility ? 
            baseAgility + " → " + agility + " ↑" : String.valueOf(agility);
        
        System.out.println("Strength: " + strDisplay + 
                         " | Dexterity: " + dexDisplay + 
                         " | Agility: " + agiDisplay);
        System.out.println("Gold: " + gold);
        System.out.println("Weapon: " + (equippedWeapon != null ? equippedWeapon.getName() : "None"));
        System.out.println("Armor: " + (equippedArmor != null ? equippedArmor.getName() : "None"));
        
        if (currentTerrain != TileType.PLAIN && currentTerrain != TileType.NEXUS) {
            System.out.println("Terrain: " + currentTerrain + " (Active Buff!)");
        }
    }

    @Override
    public String toString() {
        return name + " | HP: " + hp + " | MP: " + mana + " | Level: " + level;
    }
}