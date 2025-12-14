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
                if (c == 2 || c == 5)
                    grid[r][c] = new Tile(TileType.INACCESSIBLE);
                else if (r == 0 || r == size - 1)
                    grid[r][c] = new Tile(TileType.NEXUS);
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

    @Override
    public void display() {
        for (int r = 0; r < size; r++) {
            // Top of tile box
            System.out.print(" ");
            for (int c = 0; c < size; c++) {
                String s = grid[r][c].getBaseSymbol();
                System.out.printf(" %s - %s - %s ", s, s, s);
            }
            System.out.println();

            // Middle of tile box
            System.out.print(" ");
            for  (int c = 0; c < size; c++) {
                Tile tile = grid[r][c];
                System.out.print(" | ");

                if (tile.getType() == TileType.INACCESSIBLE) {
                    System.out.print("X X X | ");
                    continue;
                }

                if (tile.hasHero()) {
                    int heroId = tile.getHeroId();
                    System.out.print(heroId > 0 ? ("H" + heroId + " ") : "H  ");
                } else {
                    System.out.print("   ");
                }

                if (tile.hasMonster()) {
                    int monsterId = tile.getMonsterId();
                    System.out.print(monsterId > 0 ? ("M" + monsterId + " ") : "M  ");
                } else {
                    System.out.print("   ");
                }
                System.out.print("| ");
            }
            System.out.println();

            // Bottom of tile box
            System.out.print(" ");
            for (int c = 0; c < size; c++) {
                String s = grid[r][c].getBaseSymbol();
                System.out.printf(" %s - %s - %s ", s, s, s);
            }
            System.out.println();
        }
    }
}

