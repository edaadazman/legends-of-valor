package data.dataloader;

import items.Spell;
import items.SpellType;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads spell data from files.
 * Handles Fire, Ice, and Lightning spells.
 */
public class SpellDataLoader extends DataLoader<Spell> {
    private SpellType spellType;

    public SpellDataLoader(SpellType spellType) {
        this.spellType = spellType;
    }

    /**
     * Load all spells of all types.
     */
    public List<Spell> loadAllSpells() {
        List<Spell> allSpells = new ArrayList<>();
        
        allSpells.addAll(loadFromFile("FireSpells.txt", SpellType.FIRE));
        allSpells.addAll(loadFromFile("IceSpells.txt", SpellType.ICE));
        allSpells.addAll(loadFromFile("LightningSpells.txt", SpellType.LIGHTNING));
        
        return allSpells;
    }

    /**
     * Load spells from a file with a specific type.
     */
    public List<Spell> loadFromFile(String filename, SpellType type) {
        this.spellType = type;
        return super.loadFromFile(filename);
    }

    @Override
    protected Spell parseLine(String line) {
        // Format: Name/cost/required level/damage/mana cost
        String[] parts = splitLine(line, 5);
        if (parts == null) {
            return null;
        }

        try {
            String name = parts[0];
            Integer price = parseInt(parts[1], "price");
            Integer level = parseInt(parts[2], "level");
            Integer damage = parseInt(parts[3], "damage");
            Integer manaCost = parseInt(parts[4], "mana cost");

            if (price == null || level == null || damage == null || manaCost == null) {
                return null;
            }

            return new Spell(name, price, level, damage, manaCost, spellType);
        } catch (Exception e) {
            System.out.println("Error parsing spell: " + line);
            return null;
        }
    }
}