package items;

import characters.Hero;

/**
 * Class for a potion that can increase hero stats.
 */
public class Potion extends Item {
    private PotionType potionType;
    private int effectAmount;

    public Potion(String name, int price, int requiredLevel, int effectAmount, PotionType potionType) {
        super(name, price, requiredLevel, 1);
        this.potionType = potionType;
        this.effectAmount = effectAmount;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public int getEffectAmount() {
        return effectAmount;
    }

    /**
     * Apply potion effect to a hero.
     */
    public void applyEffect(Hero hero) {
        switch (potionType) {
            case HEALTH:
                hero.setHp(hero.getHp() + effectAmount);
                System.out.println(hero.getName() + " recovered " + effectAmount + " HP!");
                break;
            case MANA:
                hero.setMana(hero.getMana() + effectAmount);
                System.out.println(hero.getName() + " recovered " + effectAmount + " MP!");
                break;
            case STRENGTH:
                hero.setStrength(hero.getStrength() + effectAmount);
                System.out.println(hero.getName() + " gained " + effectAmount + " Strength!");
                break;
            case DEXTERITY:
                hero.setDexterity(hero.getDexterity() + effectAmount);
                System.out.println(hero.getName() + " gained " + effectAmount + " Dexterity!");
                break;
            case AGILITY:
                hero.setAgility(hero.getAgility() + effectAmount);
                System.out.println(hero.getName() + " gained " + effectAmount + " Agility!");
                break;
        }
    }

    @Override
    public String getType() {
        return "Potion";
    }

    @Override
    public String toString() {
        return name + " | Effect: " + potionType + " +" + effectAmount;
    }
}
