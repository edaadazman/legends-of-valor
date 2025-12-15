package data.dataloader;

import items.Weapon;
import java.util.List;

/**
 * Loads weapon data from Weaponry.txt.
 */
public class WeaponDataLoader extends DataLoader<Weapon> {

    public WeaponDataLoader() {
        super();
    }

    /**
     * Load all weapons.
     */
    public List<Weapon> loadWeapons() {
        return loadFromFile("Weaponry.txt");
    }

    @Override
    protected Weapon parseLine(String line) {
        // Format: Name/cost/level/damage/required hands
        String[] parts = splitLine(line, 5);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer price = parseInt(parts[1], "price");
            Integer level = parseInt(parts[2], "level");
            Integer damage = parseInt(parts[3], "damage");
            Integer hands = parseInt(parts[4], "hands");

            if (price == null || level == null || damage == null || hands == null) {
                return null;
            }

            return new Weapon(name, price, level, damage, hands);
        } catch (Exception e) {
            System.out.println("Error parsing weapon: " + line);
            return null;
        }
    }
}