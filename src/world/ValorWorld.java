package world;

public class ValorWorld extends World{

    private static final int DEFAULT_VALOR_WORLD_SIZE = 8;
    private static final double BUSH_RATIO = 0.20;
    private static final double CAVE_RATIO = 0.20;
    private static final double KOULOU_RATIO = 0.20;


    public ValorWorld() {
        super(DEFAULT_VALOR_WORLD_SIZE);
        this.setMovementStrategy(new ValorMovementStrategy());
    }

    @Override
    protected void generate() {

        int total = (size - 2) * (size - 2); // Subtract nexus and inaccessible spaces
        int bush = (int) (total * BUSH_RATIO);
        int cave = (int) (total * CAVE_RATIO);
        int koulou = (int) (total * KOULOU_RATIO);

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // Example: create two-wide lanes
                if (c == 2 || c == 5)
                    grid[r][c] = new Tile(TileType.INACCESSIBLE);
                else
                    grid[r][c] = new Tile(TileType.PLAIN);
            }
        }

        placeTiles(TileType.BUSH, bush);
        placeTiles(TileType.CAVE, cave);
        placeTiles(TileType.KOULOU, koulou);
    }

    @Override
    protected void placeTiles(TileType type, int count) {
        int placed = 0;
        while (placed < count) {
            int r = random.nextInt(size - 2) + 1;
            int c = random.nextInt(size);
            if (c != 2 && c != 5 && grid[r][c].getType() == TileType.PLAIN) {
                grid[r][c] = new Tile(type);
                placed++;
            }
        }
    }
}

