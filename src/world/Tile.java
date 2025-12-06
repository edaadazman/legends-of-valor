package world;

/**
 * Class for a single tile in the game world.
 */
public class Tile {
    private TileType type;
    private boolean hasParty;

    public Tile(TileType type) {
        this.type = type;
        this.hasParty = false;
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

    /**
     * Get display symbol for this tile.
     */
    public String getSymbol() {
        if (hasParty) {
            return "P";
        }
        switch (type) {
            case MARKET:
                return "M";
            case INACCESSIBLE:
                return "X";
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
