package characters;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing a party of heroes.
 */
public class Party {
    private List<Hero> heroes;

    public Party() {
        this.heroes = new ArrayList<>();
    }

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public int size() {
        return heroes.size();
    }

    public Hero getHero(int index) {
        return heroes.get(index);
    }

    public boolean allFainted() {
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getHighestLevel() {
        int maxLevel = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > maxLevel) {
                maxLevel = hero.getLevel();
            }
        }
        return maxLevel;
    }

    public void displayParty() {
        System.out.println("\n=== Party ===");
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i));
        }
    }

    public void displayDetailedStats() {
        System.out.println("\n=== Party Details ===");
        for (Hero hero : heroes) {
            hero.displayStats();
            System.out.println();
        }
    }
}
