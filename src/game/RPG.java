package game;

import characters.Hero;
import characters.Party;
import data.GameDatabase;
import items.Armor;
import items.Potion;
import items.Spell;
import items.Weapon;
import util.InputHelper;
import java.util.List;

/**
 * Abstract RPG game with common RPG mechanics.
 * Contains shared inventory management and party setup logic.
 */
public abstract class RPG extends Game {
    protected GameDatabase database;
    protected MarketEngine marketEngine;
    protected Party party;

    public RPG() {
        super();
        this.database = GameDatabase.getInstance();
        this.marketEngine = new MarketEngine();
    }

    protected void setupParty() {
        int numHeroes = getRequiredHeroCount();
        setupParty(numHeroes);
    }

    /**
     * Get the required number of heroes for this game type.
     */
    protected abstract int getRequiredHeroCount();

    /**
     * Assemble a party with the specified number of heroes.
     */
    protected void setupParty(int numHeroes) {
        System.out.println("=== ASSEMBLE YOUR PARTY ===");

        party = new Party();
        List<Hero> availableHeroes = database.getAllHeroes();

        System.out.println("\nLegendary Heroes Available for Recruitment:");
        for (int i = 0; i < availableHeroes.size(); i++) {
            Hero hero = availableHeroes.get(i);
            System.out.println((i + 1) + ") " + hero.getName() + " [" + hero.getHeroType() + "]");
        }

        for (int i = 0; i < numHeroes; i++) {
            int choice = InputHelper.readInt("\nRecruit hero #" + (i + 1) + ": ", 1, availableHeroes.size());
            Hero selectedHero = availableHeroes.get(choice - 1);

            Hero hero = new Hero(
                    selectedHero.getName(),
                    selectedHero.getLevel(),
                    selectedHero.getHeroType(),
                    selectedHero.getMana(),
                    selectedHero.getStrength(),
                    selectedHero.getDexterity(),
                    selectedHero.getAgility(),
                    selectedHero.getGold());

            party.addHero(hero);
            System.out.println(hero.getName() + " has joined your party!");
        }
        System.out.println();
    }

    /**
     * Display party information.
     */
    protected void displayInfo() {
        party.displayDetailedStats();
    }

    // ========== SHARED INVENTORY MANAGEMENT ==========

    /**
     * Manage hero's inventory and equipment.
     * Returns true if an action that consumes a turn was taken.
     */
    protected boolean manageInventory(Hero hero) {
        System.out.println("\n=== " + hero.getName() + "'s Equipment & Items ===");
        System.out.println("1) Equip Weapon");
        System.out.println("2) Equip Armor");
        System.out.println("3) Use Potion");
        System.out.println("4) View Inventory");
        System.out.println("5) Cancel");

        int choice = InputHelper.readInt("Choose action: ", 1, 5);

        switch (choice) {
            case 1:
                return equipWeapon(hero);
            case 2:
                return equipArmor(hero);
            case 3:
                return usePotion(hero);
            case 4:
                viewInventory(hero);
                return false; // Viewing doesn't consume turn
            case 5:
                return false; // Cancel doesn't consume turn
            default:
                return false;
        }
    }

    /**
     * Equip a weapon from inventory.
     * Returns true if equipment changed (consumes turn in Valor).
     */
    protected boolean equipWeapon(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("No weapons in inventory.");
            return false;
        }

        System.out.println("\nWeapons:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.println((i + 1) + ") " + w.getName() + 
                " | Damage: " + w.getDamage() + 
                " | Hands: " + w.getHandsRequired() +
                " | Level: " + w.getRequiredLevel());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose weapon: ", 0, weapons.size());
        if (choice == 0) {
            return false;
        }

        Weapon weapon = weapons.get(choice - 1);

        if (hero.getLevel() < weapon.getRequiredLevel()) {
            System.out.println("You must be level " + weapon.getRequiredLevel() + 
                " to equip " + weapon.getName() + "!");
            return false;
        }

        hero.getInventory().removeItem(weapon);
        hero.equipWeapon(weapon);
        System.out.println(hero.getName() + " equipped " + weapon.getName() + "!");

        return true; // Equipment changed
    }

    /**
     * Equip armor from inventory.
     * Returns true if equipment changed (consumes turn in Valor).
     */
    protected boolean equipArmor(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmor();

        if (armors.isEmpty()) {
            System.out.println("No armor in inventory.");
            return false;
        }

        System.out.println("\nArmor:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.println((i + 1) + ") " + a.getName() + 
                " | Defense: " + a.getDamageReduction() +
                " | Level: " + a.getRequiredLevel());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose armor: ", 0, armors.size());
        if (choice == 0) {
            return false;
        }

        Armor armor = armors.get(choice - 1);

        if (hero.getLevel() < armor.getRequiredLevel()) {
            System.out.println("You must be level " + armor.getRequiredLevel() + 
                " to equip " + armor.getName() + "!");
            return false;
        }

        hero.getInventory().removeItem(armor);
        hero.equipArmor(armor);
        System.out.println(hero.getName() + " equipped " + armor.getName() + "!");

        return true; // Equipment changed
    }

    /**
     * Use a potion from inventory.
     * Returns true (consumes turn in Valor).
     */
    protected boolean usePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("No potions in inventory.");
            return false;
        }

        System.out.println("\nPotions:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ") " + potions.get(i));
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose potion: ", 0, potions.size());
        if (choice == 0) {
            return false;
        }

        Potion potion = potions.get(choice - 1);
        potion.applyEffect(hero);
        hero.getInventory().removeItem(potion);
        System.out.println(hero.getName() + " used " + potion.getName() + "!");

        return true; // Potion used
    }

    /**
     * View hero's full inventory.
     * Does not consume turn.
     */
    protected void viewInventory(Hero hero) {
        System.out.println("\n=== " + hero.getName() + "'s Full Inventory ===");

        List<Weapon> weapons = hero.getInventory().getWeapons();
        List<Armor> armors = hero.getInventory().getArmor();
        List<Potion> potions = hero.getInventory().getPotions();
        List<Spell> spells = hero.getInventory().getSpells();

        System.out.println("\nWeapons:");
        if (weapons.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Weapon w : weapons) {
                System.out.println("  - " + w);
            }
        }

        System.out.println("\nArmor:");
        if (armors.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Armor a : armors) {
                System.out.println("  - " + a);
            }
        }

        System.out.println("\nPotions:");
        if (potions.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Potion p : potions) {
                System.out.println("  - " + p);
            }
        }

        System.out.println("\nSpells:");
        if (spells.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Spell s : spells) {
                System.out.println("  - " + s);
            }
        }

        System.out.println("\nCurrently Equipped:");
        System.out.println("  Weapon: " + (hero.getEquippedWeapon() != null ? 
            hero.getEquippedWeapon().getName() : "None"));
        System.out.println("  Armor: " + (hero.getEquippedArmor() != null ? 
            hero.getEquippedArmor().getName() : "None"));
    }

    protected void endGame() {
        System.out.println("\nThanks for playing! Safe travels, hero!\n");
    }
}