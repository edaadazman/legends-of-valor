package data;

import characters.*;
import items.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that loads and stores all game data.
 */
public class GameDatabase {
    private static GameDatabase instance;
    private static final String DATA_PATH = "Legends_Monsters_and_Heroes/";

    private List<Hero> warriorTemplates;
    private List<Hero> sorcererTemplates;
    private List<Hero> paladinTemplates;
    private List<Monster> dragonTemplates;
    private List<Monster> spiritTemplates;
    private List<Monster> exoskeletonTemplates;
    private List<Weapon> weapons;
    private List<Armor> armors;
    private List<Potion> potions;
    private List<Spell> fireSpells;
    private List<Spell> iceSpells;
    private List<Spell> lightningSpells;

    private Random random;

    private GameDatabase() {
        this.random = new Random();
        loadAllData();
    }

    public static GameDatabase getInstance() {
        if (instance == null) {
            instance = new GameDatabase();
        }
        return instance;
    }

    /**
     * Load all game data from files.
     */
    private void loadAllData() {
        warriorTemplates = loadHeroes("Warriors.txt", HeroType.WARRIOR);
        sorcererTemplates = loadHeroes("Sorcerers.txt", HeroType.SORCERER);
        paladinTemplates = loadHeroes("Paladins.txt", HeroType.PALADIN);

        dragonTemplates = loadMonsters("Dragons.txt", MonsterType.DRAGON);
        spiritTemplates = loadMonsters("Spirits.txt", MonsterType.SPIRIT);
        exoskeletonTemplates = loadMonsters("Exoskeletons.txt", MonsterType.EXOSKELETON);

        weapons = loadWeapons("Weaponry.txt");
        armors = loadArmor("Armory.txt");
        potions = loadPotions("Potions.txt");

        fireSpells = loadSpells("FireSpells.txt", SpellType.FIRE);
        iceSpells = loadSpells("IceSpells.txt", SpellType.ICE);
        lightningSpells = loadSpells("LightningSpells.txt", SpellType.LIGHTNING);
    }

    /**
     * Load heroes from a file.
     */
    private List<Hero> loadHeroes(String filename, HeroType heroType) {
        List<Hero> heroes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 7) {
                    String name = parts[0];
                    int mana = Integer.parseInt(parts[1]);
                    int strength = Integer.parseInt(parts[2]);
                    int agility = Integer.parseInt(parts[3]);
                    int dexterity = Integer.parseInt(parts[4]);
                    int gold = Integer.parseInt(parts[5]);

                    Hero hero = new Hero(name, 1, heroType, mana, strength, dexterity, agility, gold);
                    heroes.add(hero);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return heroes;
    }

    /**
     * Load monsters from a file.
     */
    private List<Monster> loadMonsters(String filename, MonsterType monsterType) {
        List<Monster> monsters = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    String name = parts[0];
                    int level = Integer.parseInt(parts[1]);
                    int damage = Integer.parseInt(parts[2]);
                    int defense = Integer.parseInt(parts[3]);
                    int dodgeChance = Integer.parseInt(parts[4]);

                    Monster monster = new Monster(name, level, monsterType, damage, defense, dodgeChance);
                    monsters.add(monster);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return monsters;
    }

    /**
     * Load weapons from a file.
     */
    private List<Weapon> loadWeapons(String filename) {
        List<Weapon> weaponList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int level = Integer.parseInt(parts[2]);
                    int damage = Integer.parseInt(parts[3]);
                    int hands = Integer.parseInt(parts[4]);

                    Weapon weapon = new Weapon(name, price, level, damage, hands);
                    weaponList.add(weapon);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return weaponList;
    }

    /**
     * Load armor from a file.
     */
    private List<Armor> loadArmor(String filename) {
        List<Armor> armorList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 4) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int level = Integer.parseInt(parts[2]);
                    int damageReduction = Integer.parseInt(parts[3]);

                    int scaledReduction = (int) (damageReduction * 0.05);

                    Armor armor = new Armor(name, price, level, scaledReduction);
                    armorList.add(armor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return armorList;
    }

    /**
     * Load potions from a file.
     */
    private List<Potion> loadPotions(String filename) {
        List<Potion> potionList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int level = Integer.parseInt(parts[2]);
                    int effectAmount = Integer.parseInt(parts[3]);
                    String typeStr = parts[4];

                    PotionType potionType = parsePotionType(typeStr);
                    Potion potion = new Potion(name, price, level, effectAmount, potionType);
                    potionList.add(potion);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return potionList;
    }

    /**
     * Load spells from a file.
     */
    private List<Spell> loadSpells(String filename, SpellType spellType) {
        List<Spell> spellList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + filename))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int level = Integer.parseInt(parts[2]);
                    int damage = Integer.parseInt(parts[3]);
                    int manaCost = Integer.parseInt(parts[4]);

                    Spell spell = new Spell(name, price, level, damage, manaCost, spellType);
                    spellList.add(spell);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return spellList;
    }

    /**
     * Parse potion type from string.
     */
    private PotionType parsePotionType(String typeStr) {
        switch (typeStr.toUpperCase()) {
            case "HEALTH":
                return PotionType.HEALTH;
            case "MANA":
                return PotionType.MANA;
            case "STRENGTH":
                return PotionType.STRENGTH;
            case "DEXTERITY":
                return PotionType.DEXTERITY;
            case "AGILITY":
                return PotionType.AGILITY;
            default:
                return PotionType.HEALTH;
        }
    }

    // Get methods
    public List<Hero> getAllHeroes() {
        List<Hero> allHeroes = new ArrayList<>();
        allHeroes.addAll(warriorTemplates);
        allHeroes.addAll(sorcererTemplates);
        allHeroes.addAll(paladinTemplates);
        return allHeroes;
    }

    public List<Monster> getAllMonsters() {
        List<Monster> allMonsters = new ArrayList<>();
        allMonsters.addAll(dragonTemplates);
        allMonsters.addAll(spiritTemplates);
        allMonsters.addAll(exoskeletonTemplates);
        return allMonsters;
    }

    public Monster getRandomMonster() {
        List<Monster> allMonsters = getAllMonsters();
        return allMonsters.isEmpty() ? null : allMonsters.get(random.nextInt(allMonsters.size()));
    }

    public List<Weapon> getWeapons() {
        return new ArrayList<>(weapons);
    }

    public List<Armor> getArmors() {
        return new ArrayList<>(armors);
    }

    public List<Potion> getPotions() {
        return new ArrayList<>(potions);
    }

    public List<Spell> getAllSpells() {
        List<Spell> allSpells = new ArrayList<>();
        allSpells.addAll(fireSpells);
        allSpells.addAll(iceSpells);
        allSpells.addAll(lightningSpells);
        return allSpells;
    }

    public List<Spell> getFireSpells() {
        return new ArrayList<>(fireSpells);
    }

    public List<Spell> getIceSpells() {
        return new ArrayList<>(iceSpells);
    }

    public List<Spell> getLightningSpells() {
        return new ArrayList<>(lightningSpells);
    }
}
