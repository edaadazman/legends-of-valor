package data.dataloader;

import items.Armor;

import java.util.List;

/**
 * Loads armor data from Armory.txt.
 */
public class ArmorDataLoader extends DataLoader<Armor> {

    public ArmorDataLoader() {
        super();
    }

    /**
     * Load all armor.
     */
    public List<Armor> loadArmor() {
        return loadFromFile("Armory.txt");
    }

    @Override
    protected Armor parseLine(String line) {
        // Format: Name/cost/required level/damage reduction
        String[] parts = splitLine(line, 4);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer price = parseInt(parts[1], "price");
            Integer level = parseInt(parts[2], "level");
            Integer damageReduction = parseInt(parts[3], "damage reduction");

            if (price == null || level == null || damageReduction == null) {
                return null;
            }

            // Scale damage reduction (as in original code)
            int scaledReduction = (int) (damageReduction * 0.05);

            return new Armor(name, price, level, scaledReduction);
        } catch (Exception e) {
            System.out.println("Error parsing armor: " + line);
            return null;
        }
    }
}