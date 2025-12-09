package world;

public class MHWorld extends World {

    private static final double INACCESSIBLE_RATIO = 0.20;
    private static final double MARKET_RATIO = 0.30;
    private static final int DEFAULT_MHWORLD_SIZE = 8;

    public MHWorld() {
        this(DEFAULT_MHWORLD_SIZE);
    }

    public MHWorld(int size) {
        super(size);
        this.setMovementStrategy(new MHMovementStrategy());
        placeParty();
    }

    @Override
    protected void generate() {
        int total = size * size;
        int inaccessible = (int) (total * INACCESSIBLE_RATIO);
        int market = (int) (total * MARKET_RATIO);

        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                grid[r][c] = new Tile(TileType.COMMON);

        placeTiles(TileType.INACCESSIBLE, inaccessible);
        placeTiles(TileType.MARKET, market);
    }

    protected void placeTiles(TileType type, int count) {
        int placed = 0;
        while (placed < count) {
            int r = random.nextInt(size);
            int c = random.nextInt(size);
            if (grid[r][c].getType() == TileType.COMMON) {
                grid[r][c] = new Tile(type);
                placed++;
            }
        }
    }
}

