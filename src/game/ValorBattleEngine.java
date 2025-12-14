package game;

import characters.Hero;
import characters.Monster;
import characters.Party;
import items.Armor;
import items.Potion;
import items.Spell;
import items.Weapon;
import util.InputHelper;
import world.Tile;
import world.TileType;
import world.ValorWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles combat mechanics for Legends of Valor.
 */
public class ValorBattleEngine {
    private static final double TERRAIN_BONUS = 0.10;

    private Random random;
    private ValorWorld world;
    private Party party;
    private List<Monster> monsters;

    public ValorBattleEngine(ValorWorld world, Party party, List<Monster> monsters) {
        this.random = new Random();
        this.world = world;
        this.party = party;
        this.monsters = monsters;
    }

    /**
     * Get monsters within attack range of a hero.
     */
    public List<Monster> getMonstersInRange(Hero hero) {
        List<Monster> inRange = new ArrayList<>();
        int heroRow = hero.getRow();
        int heroCol = hero.getCol();

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                Tile tile = world.getTile(heroRow + dr, heroCol + dc);
                if (tile != null && tile.hasMonster()) {
                    inRange.add(tile.getMonster());
                }
            }
        }
        return inRange;
    }

    /**
     * Get heroes within attack range of a monster.
     */
    public List<Hero> getHeroesInRange(Monster monster) {
        List<Hero> inRange = new ArrayList<>();
        int monsterRow = monster.getRow();
        int monsterCol = monster.getCol();

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                Tile tile = world.getTile(monsterRow + dr, monsterCol + dc);
                if (tile != null && tile.hasHero() && tile.getHero().isAlive()) {
                    inRange.add(tile.getHero());
                }
            }
        }
        return inRange;
    }

    /**
     * Get effective strength with terrain bonus (Koulou: +10% strength).
     */
    public int getEffectiveStrength(Hero hero) {
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int str = hero.getStrength();
        if (tile != null && tile.getType() == TileType.KOULOU) {
            str = (int) (str * (1 + TERRAIN_BONUS));
        }
        return str;
    }

    /**
     * Get effective dexterity with terrain bonus (Bush: +10% dexterity).
     */
    public int getEffectiveDexterity(Hero hero) {
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int dex = hero.getDexterity();
        if (tile != null && tile.getType() == TileType.BUSH) {
            dex = (int) (dex * (1 + TERRAIN_BONUS));
        }
        return dex;
    }

    /**
     * Get effective agility with terrain bonus (Cave: +10% agility).
     */
    public int getEffectiveAgility(Hero hero) {
        Tile tile = world.getTile(hero.getRow(), hero.getCol());
        int agi = hero.getAgility();
        if (tile != null && tile.getType() == TileType.CAVE) {
            agi = (int) (agi * (1 + TERRAIN_BONUS));
        }
        return agi;
    }

    /**
     * Calculate hero dodge chance with terrain bonus.
     */
    public double getEffectiveDodgeChance(Hero hero) {
        int effectiveAgility = getEffectiveAgility(hero);
        return effectiveAgility * 0.0002;
    }

    /**
     * Calculate hero attack damage with terrain bonus.
     */
    public int calculateHeroAttackDamage(Hero hero) {
        int effectiveStrength = getEffectiveStrength(hero);
        int weaponDamage = hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getDamage() : 0;
        return (int) ((effectiveStrength + weaponDamage) * 0.05);
    }

    /**
     * Hero attacks a monster. Returns true if action was taken.
     */
    public boolean heroAttack(Hero hero) {
        List<Monster> monstersInRange = getMonstersInRange(hero);

        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in range to attack!");
            return false;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster m = monstersInRange.get(i);
            System.out.println((i + 1) + ") " + m.getName() + " (HP: " + m.getHp() + ")");
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("", 0, monstersInRange.size());
        if (choice == 0) {
            return false;
        }

        Monster target = monstersInRange.get(choice - 1);

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println(target.getName() + " dodged the attack!");
            return true;
        }

        int damage = calculateHeroAttackDamage(hero);
        damage = Math.max(0, damage - target.getDefense());

        target.takeDamage(damage);
        System.out.println(hero.getName() + " strikes " + target.getName() + " for " + damage + " damage!");

        if (target.isFainted()) {
            handleMonsterDeath(target);
        }

        return true;
    }

    /**
     * Hero casts a spell on a monster. Returns true if action was taken.
     */
    public boolean heroCastSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();

        if (spells.isEmpty()) {
            System.out.println(hero.getName() + " has no spells!");
            return false;
        }

        List<Monster> monstersInRange = getMonstersInRange(hero);
        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in range to cast spells on!");
            return false;
        }

        System.out.println("\nAvailable Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println((i + 1) + ") " + s.getName() + " | Damage: " + s.getDamage() +
                    " | Mana: " + s.getManaCost() + " | Type: " + s.getSpellType());
        }
        System.out.println("0) Cancel");

        int spellChoice = InputHelper.readInt("Choose spell: ", 0, spells.size());
        if (spellChoice == 0) {
            return false;
        }

        Spell spell = spells.get(spellChoice - 1);

        if (hero.getMana() < spell.getManaCost()) {
            System.out
                    .println("Insufficient mana! Need " + spell.getManaCost() + " MP, have " + hero.getMana() + " MP.");
            return false;
        }

        System.out.println("\nSelect target:");
        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster m = monstersInRange.get(i);
            System.out.println((i + 1) + ") " + m.getName() + " (HP: " + m.getHp() + ")");
        }
        System.out.println("0) Cancel");

        int targetChoice = InputHelper.readInt("", 0, monstersInRange.size());
        if (targetChoice == 0) {
            return false;
        }

        Monster target = monstersInRange.get(targetChoice - 1);

        hero.setMana(hero.getMana() - spell.getManaCost());

        if (random.nextDouble() < target.getDodgeChance()) {
            System.out.println(target.getName() + " evades the magical assault!");
            hero.getInventory().removeItem(spell);
            return true;
        }

        int effectiveDex = getEffectiveDexterity(hero);
        int damage = (int) (spell.getDamage() + (effectiveDex / 10000.0) * spell.getDamage());
        target.takeDamage(damage);

        System.out.println(hero.getName() + " casts " + spell.getName() + " on " +
                target.getName() + " for " + damage + " damage!");

        target.applySpellEffect(spell.getSpellType().toString());

        hero.getInventory().removeItem(spell);

        if (target.isFainted()) {
            handleMonsterDeath(target);
        }

        return true;
    }

    /**
     * Handle monster death - remove from world and award rewards.
     */
    private void handleMonsterDeath(Monster monster) {
        System.out.println(monster.getName() + " has been slain!");

        Tile tile = world.getTile(monster.getRow(), monster.getCol());
        if (tile != null) {
            tile.removeMonster();
        }

        monsters.remove(monster);

        int goldReward = 500 * monster.getLevel();
        int xpReward = 2 * monster.getLevel();

        System.out.println("All heroes receive " + goldReward + " gold and " + xpReward + " experience!");

        for (Hero hero : party.getHeroes()) {
            hero.addGold(goldReward);
            hero.addExperience(xpReward);
        }
    }

    /**
     * Hero uses a potion. Returns true if action was taken.
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

        return true;
    }

    /**
     * Hero changes equipment. Returns true if equipment was changed.
     */
    public boolean heroChangeEquipment(Hero hero) {
        System.out.println("\nChange equipment:");
        System.out.println("1) Equip Weapon");
        System.out.println("2) Equip Armor");
        System.out.println("3) Back");

        int choice = InputHelper.readInt("", 1, 3);

        switch (choice) {
            case 1:
                return equipWeapon(hero);
            case 2:
                return equipArmor(hero);
            default:
                return false;
        }
    }

    private boolean equipWeapon(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("No weapons in inventory.");
            return false;
        }

        System.out.println("\nWeapons:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.println(
                    (i + 1) + ") " + w.getName() + " | Damage: " + w.getDamage() + " | Hands: " + w.getHandsRequired());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose weapon: ", 0, weapons.size());
        if (choice == 0) {
            return false;
        }

        Weapon weapon = weapons.get(choice - 1);
        hero.getInventory().removeItem(weapon);
        hero.equipWeapon(weapon);
        System.out.println(hero.getName() + " equipped " + weapon.getName() + ".");
        return true;
    }

    private boolean equipArmor(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmor();

        if (armors.isEmpty()) {
            System.out.println("No armor in inventory.");
            return false;
        }

        System.out.println("\nArmor:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.println((i + 1) + ") " + a.getName() + " | Defense: " + a.getDamageReduction());
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose armor: ", 0, armors.size());
        if (choice == 0) {
            return false;
        }

        Armor armor = armors.get(choice - 1);
        hero.getInventory().removeItem(armor);
        hero.equipArmor(armor);
        System.out.println(hero.getName() + " equipped " + armor.getName() + ".");
        return true;
    }

    /**
     * Execute monster turn - attack if hero in range, otherwise move south.
     */
    public void executeMonsterTurn(Monster monster) {
        List<Hero> heroesInRange = getHeroesInRange(monster);

        if (!heroesInRange.isEmpty()) {
            Hero target = heroesInRange.get(random.nextInt(heroesInRange.size()));
            monsterAttack(monster, target);
        } else {
            boolean moved = world.moveMonster(monster);
            if (moved) {
                System.out.println(monster.getName() + " advances south.");
            }
        }
    }

    /**
     * Monster attacks a hero.
     */
    private void monsterAttack(Monster monster, Hero hero) {
        if (random.nextDouble() < getEffectiveDodgeChance(hero)) {
            System.out.println(hero.getName() + " dodged " + monster.getName() + "'s attack!");
            return;
        }

        int damage = monster.calculateAttackDamage();
        damage = Math.max(0, damage - hero.getDamageReduction());

        hero.takeDamage(damage);
        System.out.println(monster.getName() + " attacks " + hero.getName() + " for " + damage + " damage!");

        if (hero.isFainted()) {
            System.out.println(hero.getName() + " has fallen! They will respawn at their Nexus next round.");
        }
    }

    /**
     * Check if a hero has reached the monster Nexus (win condition).
     */
    public boolean checkHeroVictory() {
        for (Hero hero : party.getHeroes()) {
            if (hero.isAlive() && hero.getRow() == 0) {
                Tile tile = world.getTile(hero.getRow(), hero.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n=== VICTORY! ===");
                    System.out.println(hero.getName() + " has reached the Monster Nexus!");
                    System.out.println("The heroes have won the battle!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if a monster has reached the hero Nexus (lose condition).
     */
    public boolean checkMonsterVictory() {
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster.getRow() == world.getSize() - 1) {
                Tile tile = world.getTile(monster.getRow(), monster.getCol());
                if (tile != null && tile.getType() == TileType.NEXUS) {
                    System.out.println("\n=== DEFEAT! ===");
                    System.out.println(monster.getName() + " has reached the Hero Nexus!");
                    System.out.println("The monsters have won. Game Over!");
                    return true;
                }
            }
        }
        return false;
    }
}
