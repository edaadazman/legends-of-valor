package game;

import characters.*;
import items.*;
import util.InputHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles all battle logic.
 */
public class BattleEngine {
    private Party party;
    private List<Monster> monsters;
    private Random random;

    public BattleEngine() {
        this.random = new Random();
    }

    /**
     * Start a battle between the party and monsters.
     */
    public boolean startBattle(Party party) {
        this.party = party;
        this.monsters = MonsterFactory.createMonsterGroup(party.size(), party.getHighestLevel());

        System.out.println("\n=== BATTLE COMMENCES! ===");
        System.out.println("Monsters emerge from the shadows to challenge your party!\n");

        displayBattleStatus();

        while (!isBattleOver()) {
            heroesTurn();

            if (allMonstersDefeated()) {
                heroesWin();
                return true;
            }

            monstersTurn();

            if (party.allFainted()) {
                heroesLose();
                return false;
            }

            endOfRoundRecovery();

            displayBattleStatus();
        }

        return true;
    }

    /**
     * Handle heroes' turn in battle.
     */
    private void heroesTurn() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isFainted()) {
                continue;
            }

            if (allMonstersDefeated()) {
                return;
            }

            boolean actionTaken = false;
            while (!actionTaken) {
                System.out.println("\n--- " + hero.getName() + "'s Turn ---");
                System.out.println("1) Attack");
                System.out.println("2) Cast Spell");
                System.out.println("3) Use Potion");
                System.out.println("4) Change Equipment");
                System.out.println("5) Battle Info");
                System.out.println("6) Skip Turn");

                int choice = InputHelper.readInt("", 1, 6);

                switch (choice) {
                    case 1:
                        actionTaken = heroAttack(hero);
                        break;
                    case 2:
                        actionTaken = heroCastSpell(hero);
                        break;
                    case 3:
                        actionTaken = heroUsePotion(hero);
                        break;
                    case 4:
                        heroChangeEquipment(hero);
                        break;
                    case 5:
                        displayBattleStatus();
                        break;
                    case 6:
                        System.out.println(hero.getName() + " waits and observes...");
                        actionTaken = true;
                        break;
                }

                if (allMonstersDefeated()) {
                    return;
                }
            }
        }
    }

    /**
     * Hero attacks a monster.
     */
    private boolean heroAttack(Hero hero) {
        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println(target.getName() + " dodged the attack!");
            return true;
        }

        int damage = hero.calculateAttackDamage();
        damage = Math.max(0, damage - target.getDefense());

        target.takeDamage(damage);
        System.out.println(hero.getName() + " strikes " + target.getName() + " for " + damage + " damage!");

        if (target.isFainted()) {
            System.out.println(target.getName() + " has been slain!");
            monsters.remove(target);
        }

        return true;
    }

    /**
     * Hero casts a spell on a monster.
     */
    private boolean heroCastSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println(hero.getName() + " knows no spells!");
            return false;
        }

        System.out.println("\nAvailable Spells:");
        for (int i = 0; i < spells.size(); i++) {
            System.out.println((i + 1) + ") " + spells.get(i));
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("", 0, spells.size());
        if (choice == 0) {
            return false;
        }

        Spell spell = spells.get(choice - 1);

        if (hero.getMana() < spell.getManaCost()) {
            System.out.println(
                    "⚠️  Insufficient mana! Need " + spell.getManaCost() + " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println("💨 " + target.getName() + " evades the magical assault!");
            hero.setMana(hero.getMana() - spell.getManaCost());
            hero.getInventory().removeItem(spell);
            return true;
        }

        int damage = spell.calculateDamage(hero);
        target.takeDamage(damage);

        System.out.println(hero.getName() + " cast " + spell.getName() + " on " +
                target.getName() + " for " + damage + " damage");

        target.applySpellEffect(spell.getSpellType().toString());

        hero.setMana(hero.getMana() - spell.getManaCost());
        hero.getInventory().removeItem(spell);

        if (target.isFainted()) {
            System.out.println(target.getName() + " has been killed.");
            monsters.remove(target);
        }

        return true;
    }

    /**
     * Hero uses a potion.
     */
    private boolean heroUsePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println(hero.getName() + " has no potions available.");
            return false;
        }

        System.out.println("\nAvailable Potions:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ") " + potions.get(i));
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("", 0, potions.size());
        if (choice == 0) {
            return false;
        }

        Potion potion = potions.get(choice - 1);
        potion.applyEffect(hero);
        hero.getInventory().removeItem(potion);

        return true;
    }

    /**
     * Hero changes equipment.
     */
    private void heroChangeEquipment(Hero hero) {
        System.out.println("\nChange equipment for " + hero.getName() + ":");
        System.out.println("1) Equip weapon");
        System.out.println("2) Equip armor");
        System.out.println("3) Back");

        int choice = InputHelper.readInt("", 1, 3);

        switch (choice) {
            case 1:
                equipWeapon(hero);
                break;
            case 2:
                equipArmor(hero);
                break;
            case 3:
                break;
        }
    }

    /**
     * Equip a weapon for a hero.
     */
    private void equipWeapon(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("No weapons in inventory.");
            return;
        }

        System.out.println("\nWeapons:");
        for (int i = 0; i < weapons.size(); i++) {
            System.out.println((i + 1) + ") " + weapons.get(i));
        }
        System.out.print("Choose weapon number or 0 to cancel:\n> ");

        int choice = InputHelper.readInt("", 0, weapons.size());
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
    private void equipArmor(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmor();

        if (armors.isEmpty()) {
            System.out.println("No armor in inventory.");
            return;
        }

        System.out.println("\nArmor:");
        for (int i = 0; i < armors.size(); i++) {
            System.out.println((i + 1) + ") " + armors.get(i));
        }
        System.out.print("Choose armor number or 0 to cancel:\n> ");

        int choice = InputHelper.readInt("", 0, armors.size());
        if (choice == 0) {
            return;
        }

        Armor armor = armors.get(choice - 1);
        hero.getInventory().removeItem(armor);
        hero.equipArmor(armor);
        System.out.println(hero.getName() + " equipped " + armor.getName() + ".");
    }

    /**
     * Select a monster target for attack or spell.
     */
    private Monster selectMonsterTarget() {
        if (monsters.isEmpty()) {
            return null;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            System.out.println((i + 1) + ") " + m.getName() + " (HP: " + m.getHp() + ")");
        }

        int choice = InputHelper.readInt("", 1, monsters.size());
        return monsters.get(choice - 1);
    }

    /**
     * Handle monsters' turn in battle.
     */
    private void monstersTurn() {
        System.out.println("\n--- Monsters' Turn ---");

        for (Monster monster : new ArrayList<>(monsters)) {
            if (monster.isFainted()) {
                continue;
            }

            List<Hero> aliveHeroes = new ArrayList<>();
            for (Hero hero : party.getHeroes()) {
                if (hero.isAlive()) {
                    aliveHeroes.add(hero);
                }
            }

            if (aliveHeroes.isEmpty()) {
                break;
            }

            Hero target = aliveHeroes.get(random.nextInt(aliveHeroes.size()));

            if (random.nextDouble() < target.getDodgeChance()) {
                System.out.println(target.getName() + " dodged " + monster.getName() + "'s attack!");
                continue;
            }

            int damage = monster.calculateAttackDamage();
            damage = Math.max(0, damage - target.getDamageReduction());

            target.takeDamage(damage);
            System.out.println(monster.getName() + " attacked " + target.getName() + " for " + damage + " damage.");

            if (target.isFainted()) {
                System.out.println(target.getName() + " has fallen!");
            }
        }
    }

    /**
     * End of round recovery for heroes.
     */
    private void endOfRoundRecovery() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive()) {
                hero.recover();
            }
        }
    }

    /**
     * Check if battle is over.
     */
    private boolean isBattleOver() {
        return allMonstersDefeated() || party.allFainted();
    }

    /**
     * Check if all monsters are defeated.
     */
    private boolean allMonstersDefeated() {
        return monsters.isEmpty();
    }

    /**
     * Handle heroes winning the battle.
     */
    private void heroesWin() {
        System.out.println("All monsters have been defeated!");

        int monsterCount = party.size();

        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive()) {
                int goldGain = party.getHighestLevel() * 100;
                int expGain = monsterCount * 2;

                hero.addGold(goldGain);
                hero.addExperience(expGain);

                System.out.println(hero.getName() + " gained " + goldGain + " gold and " + expGain + " experience.");
            } else {
                hero.revive();
            }
        }
    }

    /**
     * Handle heroes losing the battle.
     */
    private void heroesLose() {
        System.out.println("\n=== DEFEAT ===");
        System.out.println("Your party has fallen in battle...");
        System.out.println("GAME OVER");
    }

    /**
     * Display current battle status.
     */
    private void displayBattleStatus() {
        System.out.println("\n=== YOUR PARTY ===");
        for (Hero hero : party.getHeroes()) {
            String status = hero.isFainted() ? "FAINTED" : "FIGHTING";
            System.out.println(hero.getName() + " | HP: " + hero.getHp() + " | MP: " + hero.getMana() +
                    " | Status: " + status);
        }

        System.out.println("\n=== ENEMIES ===");
        for (Monster monster : monsters) {
            System.out.println(monster.getName() + " | Level: " + monster.getLevel() +
                    " | HP: " + monster.getHp() + " | Damage: " + monster.getBaseDamage());
        }
    }
}
