package world;

public class ValorWorld extends World{

    private static final int DEFAULT_VALOR_WORLD_SIZE = 8;

    public ValorWorld() {
        super(DEFAULT_VALOR_WORLD_SIZE);
        this.setMovementStrategy(new ValorMovementStrategy());
    }

    @Override
    protected void generate() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // Example: create two-wide lanes
                if (c == 2 || c == 5)
                    grid[r][c] = new Tile(TileType.INACCESSIBLE);
                else
                    grid[r][c] = new Tile(TileType.COMMON);
            }
        }
    }
}

