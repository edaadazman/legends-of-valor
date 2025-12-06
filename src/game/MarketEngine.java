package game;

import characters.*;
import items.*;
import data.GameDatabase;
import util.InputHelper;
import java.util.List;

/**
 * Handles all market interactions.
 */
public class MarketEngine {
    private GameDatabase database;

    public MarketEngine() {
        this.database = GameDatabase.getInstance();
    }

    /**
     * Open the market menu for the party.
     */
    public void enterMarket(Party party) {
        boolean inMarket = true;

        System.out.println("\n=== WELCOME TO THE MARKET ===");
        System.out.println("A merchant greets you warmly...\n");

        while (inMarket) {
            System.out.println("\n=== MARKET MENU ===");
            System.out.println("1) Buy Items");
            System.out.println("2) Sell Items");
            System.out.println("3) View Heroes");
            System.out.println("4) Leave Market");

            int choice = InputHelper.readInt("", 1, 4);

            switch (choice) {
                case 1:
                    buyItems(party);
                    break;
                case 2:
                    sellItems(party);
                    break;
                case 3:
                    party.displayDetailedStats();
                    break;
                case 4:
                    inMarket = false;
                    break;
            }
        }
    }

    /**
     * Buy items from the market.
     */
    private void buyItems(Party party) {
        Hero hero = selectHero(party);
        if (hero == null) {
            return;
        }

        System.out.println("\nGold Available: " + hero.getGold());
        System.out.println("\n=== Item Categories ===");
        System.out.println("1) Weapons");
        System.out.println("2) Armor");
        System.out.println("3) Potions");
        System.out.println("4) Spells");
        System.out.println("5) Back");

        int choice = InputHelper.readInt("", 1, 5);

        switch (choice) {
            case 1:
                buyWeapons(hero);
                break;
            case 2:
                buyArmor(hero);
                break;
            case 3:
                buyPotions(hero);
                break;
            case 4:
                buySpells(hero);
                break;
            case 5:
                break;
        }
    }

    /**
     * Buy weapons from the market.
     */
    private void buyWeapons(Hero hero) {
        List<Weapon> weapons = database.getWeapons();
        displayItemsForSale(weapons, hero);

        int choice = InputHelper.readInt("Choose item number or 0 to cancel\n", 0, weapons.size());
        if (choice == 0) {
            return;
        }

        Weapon weapon = weapons.get(choice - 1);
        purchaseItem(hero, weapon);
    }

    /**
     * Buy armor from the market.
     */
    private void buyArmor(Hero hero) {
        List<Armor> armors = database.getArmors();
        displayItemsForSale(armors, hero);

        int choice = InputHelper.readInt("Choose item number or 0 to cancel\n", 0, armors.size());
        if (choice == 0) {
            return;
        }

        Armor armor = armors.get(choice - 1);
        purchaseItem(hero, armor);
    }

    /**
     * Buy potions from the market.
     */
    private void buyPotions(Hero hero) {
        List<Potion> potions = database.getPotions();
        displayItemsForSale(potions, hero);

        int choice = InputHelper.readInt("Choose item number or 0 to cancel\n", 0, potions.size());
        if (choice == 0) {
            return;
        }

        Potion potion = potions.get(choice - 1);
        purchaseItem(hero, potion);
    }

    /**
     * Buy spells from the market.
     */
    private void buySpells(Hero hero) {
        System.out.println("Choose spell type:");
        System.out.println("1) Fire Spells");
        System.out.println("2) Ice Spells");
        System.out.println("3) Lightning Spells");

        int typeChoice = InputHelper.readInt("", 1, 3);

        List<Spell> spells;
        switch (typeChoice) {
            case 1:
                spells = database.getFireSpells();
                break;
            case 2:
                spells = database.getIceSpells();
                break;
            case 3:
                spells = database.getLightningSpells();
                break;
            default:
                return;
        }

        displayItemsForSale(spells, hero);

        int choice = InputHelper.readInt("Choose item number or 0 to cancel\n", 0, spells.size());
        if (choice == 0) {
            return;
        }

        Spell spell = spells.get(choice - 1);
        purchaseItem(hero, spell);
    }

    /**
     * Display items for sale.
     */
    private void displayItemsForSale(List<? extends Item> items, Hero hero) {
        System.out.println("\n=== SHOP INVENTORY ===");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String canBuy = canHeroBuyItem(hero, item) ? "" : " (Cannot buy)";
            System.out.println((i + 1) + ") " + item.getName() + " | Price: " + item.getPrice() +
                    " | Level: " + item.getRequiredLevel() + canBuy);
        }
    }

    /**
     * Purchase an item for a hero.
     */
    private void purchaseItem(Hero hero, Item item) {
        if (!canHeroBuyItem(hero, item)) {
            if (hero.getGold() < item.getPrice()) {
                System.out.println("\nInsufficient gold! You need " + item.getPrice() + " but only have "
                        + hero.getGold() + ".");
            } else if (hero.getLevel() < item.getRequiredLevel()) {
                System.out.println("\nYou must be level " + item.getRequiredLevel() + " to purchase this item!");
            }
            return;
        }

        hero.spendGold(item.getPrice());
        hero.getInventory().addItem(item);
        System.out.println(
                "\n" + hero.getName() + " purchased " + item.getName() + " for " + item.getPrice() + " gold!");
    }

    /**
     * Check if hero can buy an item.
     */
    private boolean canHeroBuyItem(Hero hero, Item item) {
        return hero.getGold() >= item.getPrice() && hero.getLevel() >= item.getRequiredLevel();
    }

    /**
     * Sell items from hero's inventory.
     */
    private void sellItems(Party party) {
        Hero hero = selectHero(party);
        if (hero == null) {
            return;
        }

        List<Item> items = hero.getInventory().getItems();

        if (items.isEmpty()) {
            System.out.println(hero.getName() + " has no items to sell.");
            return;
        }

        System.out.println("\n=== " + hero.getName() + "'s Inventory ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ") " + items.get(i) + " | Sell Price: " + items.get(i).getSellPrice());
        }

        int choice = InputHelper.readInt("Choose item to sell or 0 to cancel\n", 0, items.size());
        if (choice == 0) {
            return;
        }

        Item item = items.get(choice - 1);
        hero.getInventory().removeItem(item);
        hero.addGold(item.getSellPrice());
        System.out.println(hero.getName() + " sold " + item.getName() + " for " + item.getSellPrice() + " gold!");
    }

    /**
     * Select a hero from the party.
     */
    private Hero selectHero(Party party) {
        System.out.println("\n=== Select Hero ===");
        for (int i = 0; i < party.size(); i++) {
            Hero h = party.getHero(i);
            System.out.println((i + 1) + ") " + h.getName() + " (" + h.getGold() + " gold)");
        }

        int choice = InputHelper.readInt("", 1, party.size());
        return party.getHero(choice - 1);
    }
}
