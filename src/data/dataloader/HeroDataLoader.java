package data.dataloader;

import characters.Hero;
import characters.HeroType;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads hero data from files.
 * Handles Warriors, Sorcerers, and Paladins.
 */
public class HeroDataLoader extends DataLoader<Hero> {
    private HeroType heroType;

    public HeroDataLoader(HeroType heroType) {
        this.heroType = heroType;
    }

    /**
     * Load all heroes of all types.
     */
    public List<Hero> loadAllHeroes() {
        List<Hero> allHeroes = new ArrayList<>();
        
        allHeroes.addAll(loadFromFile("Warriors.txt", HeroType.WARRIOR));
        allHeroes.addAll(loadFromFile("Sorcerers.txt", HeroType.SORCERER));
        allHeroes.addAll(loadFromFile("Paladins.txt", HeroType.PALADIN));
        
        return allHeroes;
    }

    /**
     * Load heroes from a file with a specific type.
     */
    public List<Hero> loadFromFile(String filename, HeroType type) {
        this.heroType = type;
        return super.loadFromFile(filename);
    }

    @Override
    protected Hero parseLine(String line) {
        // Format: Name/mana/strength/agility/dexterity/starting money/starting experience
        String[] parts = splitLine(line, 7);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer mana = parseInt(parts[1], "mana");
            Integer strength = parseInt(parts[2], "strength");
            Integer agility = parseInt(parts[3], "agility");
            Integer dexterity = parseInt(parts[4], "dexterity");
            Integer gold = parseInt(parts[5], "gold");
            // parts[6] is starting experience (not used)

            if (mana == null || strength == null || agility == null || 
                dexterity == null || gold == null) {
                return null;
            }

            return new Hero(name, 1, heroType, mana, strength, dexterity, agility, gold);
        } catch (Exception e) {
            System.out.println("Error parsing hero: " + line);
            return null;
        }
    }
}