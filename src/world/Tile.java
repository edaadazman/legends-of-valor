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
    private int heroId;
    private Monster monster;

    public Tile(TileType type) {
        this.type = type;
        this.hasParty = false;
        this.hero = null;
        this.heroId = 0;
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

    /** Set the hero occupant for this tile. */
    public void setHero(Hero hero) {
        this.hero = hero;
        this.heroId = (hero == null) ? 0 : this.heroId;
    }

    /** Set the hero occupant for this tile along with a display id (1-3). */
    public void setHero(Hero hero, int heroId) {
        this.hero = hero;
        this.heroId = (hero == null) ? 0 : heroId;
    }

    /** Get the display id for the hero on this tile (0 if none). */
    public int getHeroId() { return heroId; }
    public boolean hasHero() { return hero != null; }

    /** Remove the hero occupant (and its display id) from this tile. */
    public void removeHero() {
        this.hero = null;
        this.heroId = 0;
    }

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
        if (hero != null) return heroId > 0 ? ("H" + heroId) : "H";
        if (monster != null) return "M";

        return getBaseSymbol();
    }

    /** Get the base (terrain) symbol for this tile, ignoring occupants. */
    public String getBaseSymbol() {

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
            case PLAIN:
                return "P";
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