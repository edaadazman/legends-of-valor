package data.dataloader;

import characters.Monster;
import characters.MonsterType;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads monster data from files.
 * Handles Dragons, Spirits, and Exoskeletons.
 */
public class MonsterDataLoader extends DataLoader<Monster> {
    private MonsterType monsterType;

    public MonsterDataLoader(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    /**
     * Load all monsters of all types.
     */
    public List<Monster> loadAllMonsters() {
        List<Monster> allMonsters = new ArrayList<>();
        
        allMonsters.addAll(loadFromFile("Dragons.txt", MonsterType.DRAGON));
        allMonsters.addAll(loadFromFile("Spirits.txt", MonsterType.SPIRIT));
        allMonsters.addAll(loadFromFile("Exoskeletons.txt", MonsterType.EXOSKELETON));
        
        return allMonsters;
    }

    /**
     * Load monsters from a file with a specific type.
     */
    public List<Monster> loadFromFile(String filename, MonsterType type) {
        this.monsterType = type;
        return super.loadFromFile(filename);
    }

    @Override
    protected Monster parseLine(String line) {
        // Format: Name/level/damage/defense/dodge chance
        String[] parts = splitLine(line, 5);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer level = parseInt(parts[1], "level");
            Integer damage = parseInt(parts[2], "damage");
            Integer defense = parseInt(parts[3], "defense");
            Integer dodgeChance = parseInt(parts[4], "dodge chance");

            if (level == null || damage == null || defense == null || dodgeChance == null) {
                return null;
            }

            return new Monster(name, level, monsterType, damage, defense, dodgeChance);
        } catch (Exception e) {
            System.out.println("Error parsing monster: " + line);
            return null;
        }
    }
}