package world;

import characters.Hero;
import characters.Monster;

/**
 * Class for a single tile in the game world.
 */
public class Tile {
    private TileType type;
    private boolean hasParty;
    private Hero hero;
    private Monster monster;

    public Tile(TileType type) {
        this.type = type;
        this.hasParty = false;
        this.hero = null;
        this.monster = null;
    }

    public TileType getType() {
        return type;
    }

    public boolean isAccessible() {
        return type != TileType.INACCESSIBLE;
    }

    public boolean isMarket() {
        return type == TileType.MARKET;
    }

    public boolean isCommon() {
        return type == TileType.COMMON;
    }

    public boolean hasParty() {
        return hasParty;
    }

    public void setHasParty(boolean hasParty) {
        this.hasParty = hasParty;
    }

    public Hero getHero() { return hero; }
    public void setHero(Hero hero) { this.hero = hero; }
    public boolean hasHero() { return hero != null; }
    public void removeHero() { this.hero = null; }

    public Monster getMonster() { return monster; }
    public void setMonster(Monster monster) { this.monster = monster; }
    public boolean hasMonster() { return monster != null; }
    public void removeMonster() { this.monster = null; }

    /**
     * Get display symbol for this tile.
     */
    public String getSymbol() {
        if (hasParty) {
            return "P";
        }
        if (hero != null) return "H";
        if (monster != null) return "M";

        switch (type) {
            case MARKET:
                return "M";
            case INACCESSIBLE:
                return "X";
            case NEXUS:
                return "N";
            case BUSH:
                return "B";
            case CAVE:
                return "C";
            case KOULOU:
                return "K";
            case COMMON:
            default:
                return ".";
        }
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}