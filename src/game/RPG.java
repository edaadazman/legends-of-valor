package game;

import characters.Hero;
import characters.Party;
import data.GameDatabase;
import util.InputHelper;
import java.util.List;

/**
 * Abstract RPG game with common RPG mechanics.
 */
public abstract class RPG extends Game {
    protected GameDatabase database;
    protected BattleEngine battleEngine;
    protected MarketEngine marketEngine;
    protected Party party;

    public RPG() {
        super();
        this.database = GameDatabase.getInstance();
        this.battleEngine = new BattleEngine();
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

    protected void endGame() {
        System.out.println("\nThanks for playing! Safe travels, hero!\n");
    }
}