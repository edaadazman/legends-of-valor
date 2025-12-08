package world;

public class ValorWorld extends World{
    public ValorWorld(int size) {
        super(size);
        this.setMovementStrategy(new ValorMovementStrategy());
    }

    @Override
    protected void generate() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // Example: create two-wide lanes
                if (c == 0 || c == 3 || c == 6 || c == 7)
                    grid[r][c] = new Tile(TileType.INACCESSIBLE);
                else
                    grid[r][c] = new Tile(TileType.COMMON);
            }
        }
    }
}

