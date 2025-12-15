package data.dataloader;

import items.Potion;
import items.PotionType;

import java.util.List;

/**
 * Loads potion data from Potions.txt.
 */
public class PotionDataLoader extends DataLoader<Potion> {

    public PotionDataLoader() {
        super();
    }

    /**
     * Load all potions.
     */
    public List<Potion> loadPotions() {
        return loadFromFile("Potions.txt");
    }

    @Override
    protected Potion parseLine(String line) {
        // Format: Name/cost/required level/attribute increase/attribute affected
        String[] parts = splitLine(line, 5);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer price = parseInt(parts[1], "price");
            Integer level = parseInt(parts[2], "level");
            Integer effectAmount = parseInt(parts[3], "effect amount");
            String typeStr = parts[4];

            if (price == null || level == null || effectAmount == null) {
                return null;
            }

            PotionType potionType = parsePotionType(typeStr);
            return new Potion(name, price, level, effectAmount, potionType);
        } catch (Exception e) {
            System.out.println("Error parsing potion: " + line);
            return null;
        }
    }

    /**
     * Parse potion type from string.
     */
    private PotionType parsePotionType(String typeStr) {
        switch (typeStr.toUpperCase()) {
            case "HEALTH":
                return PotionType.HEALTH;
            case "MANA":
                return PotionType.MANA;
            case "STRENGTH":
                return PotionType.STRENGTH;
            case "DEXTERITY":
                return PotionType.DEXTERITY;
            case "AGILITY":
                return PotionType.AGILITY;
            default:
                System.out.println("Unknown potion type: " + typeStr + ", defaulting to HEALTH");
                return PotionType.HEALTH;
        }
    }
}