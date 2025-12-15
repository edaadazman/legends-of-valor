package combat;

import characters.Character;
import characters.Hero;
import characters.Monster;
import characters.Party;
import items.Armor;
import items.Potion;
import items.Spell;
import items.Weapon;
import util.InputHelper;
import world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class for all battle engines.
 * Contains shared combat logic to eliminate duplication.
 */
public abstract class BattleEngine {
    protected Random random;
    protected World world;
    protected Party party;
    protected List<Monster> monsters;
    protected CombatExecutor combatExecutor;

    public BattleEngine(World world, Party party, List<Monster> monsters) {
        this.random = new Random();
        this.world = world;
        this.party = party;
        this.monsters = monsters;
        this.combatExecutor = new CombatExecutor(world, party, monsters);
    }

    /**
     * Hero attacks a target.
     */
    public boolean heroAttack(Hero hero, Monster target) {
        if (hero.isFainted()) {
            System.out.println(hero.getName() + " is fainted and cannot attack!");
            return false;
        }

        if (target.isFainted()) {
            System.out.println(target.getName() + " is already defeated!");
            return false;
        }

        return combatExecutor.executeAttack(hero, target);
    }

    /**
     * Hero casts a spell on a target.
     */
    public boolean heroCastSpell(Hero hero, Spell spell, Monster target) {
        if (hero.isFainted()) {
            System.out.println(hero.getName() + " is fainted and cannot cast spells!");
            return false;
        }

        if (target.isFainted()) {
            System.out.println(target.getName() + " is already defeated!");
            return false;
        }

        if (hero.getMana() < spell.getManaCost()) {
            System.out.println("Insufficient mana!");
            return false;
        }

        // Deduct mana cost
        hero.setMana(hero.getMana() - spell.getManaCost());

        return combatExecutor.executeSpell(hero, target, spell);
    }

    /**
     * Monster attacks a hero.
     */
    protected void monsterAttack(Monster monster, Hero hero) {
        if (monster.isFainted() || hero.isFainted()) {
            return;
        }

        combatExecutor.executeAttack(monster, hero);
    }

    /**
     * Hero uses a potion.
     */
    public boolean heroUsePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println(hero.getName() + " has no potions!");
            return false;
        }

        System.out.println("\nAvailable Potions:");
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

        return true;
    }

    /**
     * Hero changes equipment.
     */
    public void heroChangeEquipment(Hero hero) {
        System.out.println("\nChange equipment:");
        System.out.println("1) Equip Weapon");
        System.out.println("2) Equip Armor");
        System.out.println("3) Back");

        int choice = InputHelper.readInt("", 1, 3);

        switch (choice) {
            case 1:
                equipWeapon(hero);
                break;
            case 2:
                equipArmor(hero);
                break;
        }
    }

    /**
     * Equip a weapon for a hero.
     */
    protected void equipWeapon(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("No weapons in inventory.");
            return;
        }

        System.out.println("\nWeapons:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.println((i + 1) + ") " + w.getName() + 
                " | Damage: " + w.getDamage() + " | Hands: " + w.getHandsRequired());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose weapon: ", 0, weapons.size());
        if (choice == 0) {
            return;
        }

        Weapon weapon = weapons.get(choice - 1);
        hero.getInventory().removeItem(weapon);
        hero.equipWeapon(weapon);
        System.out.println(hero.getName() + " equipped " + weapon.getName() + ".");
    }

    /**
     * Equip armor for a hero.
     */
    protected void equipArmor(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmor();

        if (armors.isEmpty()) {
            System.out.println("No armor in inventory.");
            return;
        }

        System.out.println("\nArmor:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.println((i + 1) + ") " + a.getName() + 
                " | Defense: " + a.getDamageReduction());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose armor: ", 0, armors.size());
        if (choice == 0) {
            return;
        }

        Armor armor = armors.get(choice - 1);
        hero.getInventory().removeItem(armor);
        hero.equipArmor(armor);
        System.out.println(hero.getName() + " equipped " + armor.getName() + ".");
    }

    /**
     * Select a monster target from a list.
     */
    protected Monster selectMonsterTarget(List<Monster> availableMonsters) {
        if (availableMonsters.isEmpty()) {
            return null;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < availableMonsters.size(); i++) {
            Monster m = availableMonsters.get(i);
            System.out.println((i + 1) + ") " + m.getName() + " (HP: " + m.getHp() + ")");
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("", 0, availableMonsters.size());
        if (choice == 0) {
            return null;
        }

        return availableMonsters.get(choice - 1);
    }

    /**
     * Select a spell from hero's inventory.
     */
    protected Spell selectSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println(hero.getName() + " has no spells!");
            return null;
        }

        System.out.println("\nAvailable Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println((i + 1) + ") " + s.getName() + " | Damage: " + s.getDamage() +
                    " | Mana: " + s.getManaCost() + " | Type: " + s.getSpellType());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose spell: ", 0, spells.size());
        if (choice == 0) {
            return null;
        }

        return spells.get(choice - 1);
    }

    /**
     * Get characters in range of a given character.
     */
    protected List<Character> getCharactersInRange(Character character, int range) {
        return combatExecutor.getCharactersInRange(character, range);
    }

    /**
     * Display combat menu for a hero.
     */
    protected void displayCombatMenu(Hero hero) {
        System.out.println("\n--- " + hero.getName() + "'s Turn ---");
        System.out.println("1) Attack");
        System.out.println("2) Cast Spell");
        System.out.println("3) Use Potion");
        System.out.println("4) Change Equipment");
        System.out.println("5) Battle Info");
        System.out.println("6) Skip Turn");
    }

    /**
     * Get party.
     */
    public Party getParty() {
        return party;
    }

    /**
     * Get monsters.
     */
    public List<Monster> getMonsters() {
        return monsters;
    }
}