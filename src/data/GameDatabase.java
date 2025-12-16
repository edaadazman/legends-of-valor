package data;

import characters.*;
import items.*;
import data.dataloader.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Facade class that provides a simple interface to all game data.
 * Uses specialized data loaders internally.
 * Implements Singleton pattern for global access.
 */
public class GameDatabase {
    private static GameDatabase instance;
    private Random random;

    // Data loaders (hidden from clients)
    private HeroDataLoader heroLoader;
    private MonsterDataLoader monsterLoader;
    private WeaponDataLoader weaponLoader;
    private ArmorDataLoader armorLoader;
    private PotionDataLoader potionLoader;
    private SpellDataLoader spellLoader;

    // Cached data
    private List<Hero> allHeroes;
    private List<Monster> allMonsters;
    private List<Weapon> weapons;
    private List<Armor> armors;
    private List<Potion> potions;
    private List<Spell> fireSpells;
    private List<Spell> iceSpells;
    private List<Spell> lightningSpells;

    /**
     * Private constructor - Singleton pattern.
     */
    private GameDatabase() {
        this.random = new Random();
        initializeLoaders();
        loadAllData();
    }

    /**
     * Get singleton instance.
     */
    public static GameDatabase getInstance() {
        if (instance == null) {
            instance = new GameDatabase();
        }
        return instance;
    }

    /**
     * Initialize all data loaders.
     */
    private void initializeLoaders() {
        this.heroLoader = new HeroDataLoader(null);
        this.monsterLoader = new MonsterDataLoader(null);
        this.weaponLoader = new WeaponDataLoader();
        this.armorLoader = new ArmorDataLoader();
        this.potionLoader = new PotionDataLoader();
        this.spellLoader = new SpellDataLoader(null);
    }

    /**
     * Load all game data using the data loaders.
     */
    private void loadAllData() {
        System.out.println("Loading game data...");

        // Load heroes
        allHeroes = heroLoader.loadAllHeroes();

        // Load monsters
        allMonsters = monsterLoader.loadAllMonsters();

        // Load items
        weapons = weaponLoader.loadWeapons();

        armors = armorLoader.loadArmor();

        potions = potionLoader.loadPotions();

        // Load spells
        fireSpells = spellLoader.loadFromFile("FireSpells.txt", SpellType.FIRE);
        iceSpells = spellLoader.loadFromFile("IceSpells.txt", SpellType.ICE);
        lightningSpells = spellLoader.loadFromFile("LightningSpells.txt", SpellType.LIGHTNING);

        System.out.println("Game data loaded successfully!\n");
    }

    // ========== PUBLIC API - Simple methods for clients ==========

    /**
     * Get all available heroes.
     */
    public List<Hero> getAllHeroes() {
        return new ArrayList<>(allHeroes);
    }

    /**
     * Get all available monsters.
     */
    public List<Monster> getAllMonsters() {
        return new ArrayList<>(allMonsters);
    }

    /**
     * Get a random monster for spawning.
     */
    public Monster getRandomMonster() {
        return allMonsters.isEmpty() ? null : allMonsters.get(random.nextInt(allMonsters.size()));
    }

    /**
     * Get all weapons.
     */
    public List<Weapon> getWeapons() {
        return new ArrayList<>(weapons);
    }

    /**
     * Get all armors.
     */
    public List<Armor> getArmors() {
        return new ArrayList<>(armors);
    }

    /**
     * Get all potions.
     */
    public List<Potion> getPotions() {
        return new ArrayList<>(potions);
    }

    /**
     * Get all spells (all types combined).
     */
    public List<Spell> getAllSpells() {
        List<Spell> allSpells = new ArrayList<>();
        allSpells.addAll(fireSpells);
        allSpells.addAll(iceSpells);
        allSpells.addAll(lightningSpells);
        return allSpells;
    }

    /**
     * Get fire spells.
     */
    public List<Spell> getFireSpells() {
        return new ArrayList<>(fireSpells);
    }

    /**
     * Get ice spells.
     */
    public List<Spell> getIceSpells() {
        return new ArrayList<>(iceSpells);
    }

    /**
     * Get lightning spells.
     */
    public List<Spell> getLightningSpells() {
        return new ArrayList<>(lightningSpells);
    }

    /**
     * Reload all game data (useful for development/testing).
     */
    public void reloadData() {
        loadAllData();
    }
}