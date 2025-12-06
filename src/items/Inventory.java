package items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages items in a hero's inventory.
 */
public class Inventory {
    private List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Weapon> getWeapons() {
        return items.stream()
                .filter(item -> item instanceof Weapon)
                .map(item -> (Weapon) item)
                .collect(Collectors.toList());
    }

    public List<Armor> getArmor() {
        return items.stream()
                .filter(item -> item instanceof Armor)
                .map(item -> (Armor) item)
                .collect(Collectors.toList());
    }

    public List<Potion> getPotions() {
        return items.stream()
                .filter(item -> item instanceof Potion)
                .map(item -> (Potion) item)
                .collect(Collectors.toList());
    }

    public List<Spell> getSpells() {
        return items.stream()
                .filter(item -> item instanceof Spell)
                .map(item -> (Spell) item)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }
}

