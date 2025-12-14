package game;

import characters.Hero;
import items.*;
import util.InputHelper;
import world.*;
import java.util.List;
import java.util.Random;

/**
 * Monsters and Heroes game implementation.
 */
public class MonstersAndHeroes extends RPG {
    private static final double BATTLE_CHANCE = 0.2;
    
    private World world;
    private Random random;

    public MonstersAndHeroes() {
        super();
        this.random = new Random();
    }

    @Override
    protected void displayWelcome() {
        System.out.println("\n===========================================");
        System.out.println("  LEGENDS: MONSTERS AND HEROES");
        System.out.println("  A Tale of Courage, Magic, and Glory");
        System.out.println("===========================================");
        System.out.println("\nThe realm is under siege by fearsome monsters...");
        System.out.println("Brave heroes are needed to restore peace to the land!\n");
    }

    @Override
    protected int getRequiredHeroCount() {
        return InputHelper.readInt("\nHow many heroes will join your quest? (1-3): ", 1, 3);
    }

    @Override
    public void start() {
        displayWelcome();
        setupParty();
        world = new MHWorld();

        System.out.println("\nYour epic journey begins!");
        System.out.println("May fortune favor the bold...\n");

        gameLoop();
        endGame();
    }

    @Override
    protected void gameLoop() {
        while (gameRunning) {
            displayWorld();
            displayControls();
            handleInput();
        }
    }

    private void displayWorld() {
        System.out.println();
        world.display();
    }

    private void displayControls() {
        System.out.println("\nControls:");
        System.out.println("W/A/S/D - Move");
        System.out.println("M - Market");
        System.out.println("I - Info");
        System.out.println("V - Inventory");
        System.out.println("Q - Quit");
        System.out.println();
    }

    private void handleInput() {
        char input = InputHelper.readChar("Enter command: ");

        switch (Character.toLowerCase(input)) {
            case 'w':
                moveParty(-1, 0);
                break;
            case 's':
                moveParty(1, 0);
                break;
            case 'a':
                moveParty(0, -1);
                break;
            case 'd':
                moveParty(0, 1);
                break;
            case 'm':
                enterMarket();
                break;
            case 'i':
                displayInfo();
                break;
            case 'v':
                manageInventory();
                break;
            case 'q':
                gameRunning = false;
                break;
            default:
                System.out.println("Invalid command!");
                break;
        }
    }

    private void moveParty(int deltaRow, int deltaCol) {
        if (world.moveHero(null, deltaRow, deltaCol)) {
            handleTileEvent();
        }
    }

    private void handleTileEvent() {
        Tile currentTile = world.getCurrentTile();

        if (currentTile.isMarket()) {
            System.out.println("\nYou've discovered a bustling marketplace!");
            System.out.println("Press [M] to browse wares and trade goods.");
        } else if (currentTile.isCommon()) {
            if (random.nextDouble() < BATTLE_CHANCE) {
                boolean won = battleEngine.startBattle(party);
                if (!won) {
                    gameRunning = false;
                }
            } else {
                System.out.println("\nThe path ahead is quiet. Your party continues onward.");
            }
        }
    }

    private void enterMarket() {
        Tile currentTile = world.getCurrentTile();

        if (!currentTile.isMarket()) {
            System.out.println("\nThere's no market here. Look for a [M] tile on the map!");
            return;
        }

        marketEngine.enterMarket(party);
    }

    private void manageInventory() {
        System.out.println("\n=== Inventory Management ===");
        System.out.println("Choose hero:");
        for (int i = 0; i < party.size(); i++) {
            System.out.println((i + 1) + ") " + party.getHero(i).getName());
        }
        System.out.println("0) Back");

        int heroChoice = InputHelper.readInt("", 0, party.size());
        if (heroChoice == 0) {
            return;
        }

        Hero hero = party.getHero(heroChoice - 1);

        boolean inInventory = true;
        while (inInventory) {
            System.out.println("\n=== " + hero.getName() + "'s Inventory ===");
            System.out.println("1) Equip Weapon");
            System.out.println("2) Equip Armor");
            System.out.println("3) Use Potion");
            System.out.println("4) View Inventory");
            System.out.println("5) Back");

            int choice = InputHelper.readInt("", 1, 5);

            switch (choice) {
                case 1:
                    equipWeaponOutsideBattle(hero);
                    break;
                case 2:
                    equipArmorOutsideBattle(hero);
                    break;
                case 3:
                    usePotionOutsideBattle(hero);
                    break;
                case 4:
                    viewInventory(hero);
                    break;
                case 5:
                    inInventory = false;
                    break;
            }
        }
    }

    private void equipWeaponOutsideBattle(Hero hero) {
        List<Weapon> weapons = hero.getInventory().getWeapons();

        if (weapons.isEmpty()) {
            System.out.println("No weapons in inventory.");
            return;
        }

        System.out.println("\nWeapons:");
        for (int i = 0; i < weapons.size(); i++) {
            System.out.println((i + 1) + ") " + weapons.get(i));
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

    private void equipArmorOutsideBattle(Hero hero) {
        List<Armor> armors = hero.getInventory().getArmor();

        if (armors.isEmpty()) {
            System.out.println("No armor in inventory.");
            return;
        }

        System.out.println("\nArmor:");
        for (int i = 0; i < armors.size(); i++) {
            System.out.println((i + 1) + ") " + armors.get(i));
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

    private void usePotionOutsideBattle(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();

        if (potions.isEmpty()) {
            System.out.println("No potions in inventory.");
            return;
        }

        System.out.println("\nPotions:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ") " + potions.get(i));
        }
        System.out.println("0) Cancel");

        int choice = InputHelper.readInt("Choose potion: ", 0, potions.size());
        if (choice == 0) {
            return;
        }

        Potion potion = potions.get(choice - 1);
        potion.applyEffect(hero);
        hero.getInventory().removeItem(potion);
    }

    private void viewInventory(Hero hero) {
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
        System.out.println("  Weapon: " + (hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getName() : "None"));
        System.out.println("  Armor: " + (hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : "None"));
    }
}