package world;

import java.util.Random;

/**
 * Class for the game world as a grid of tiles.
 */
public class World {
    private static final int DEFAULT_SIZE = 8;
    private static final double INACCESSIBLE_RATIO = 0.20;
    private static final double MARKET_RATIO = 0.30;

    private Tile[][] grid;
    private int size;
    private int partyRow;
    private int partyCol;
    private Random random;

    public World() {
        this(DEFAULT_SIZE);
    }

    public World(int size) {
        this.size = size;
        this.grid = new Tile[size][size];
        this.random = new Random();
        generateWorld();
        placeParty();
    }

    /**
     * Generate the world grid with different tile types.
     */
    private void generateWorld() {
        int totalTiles = size * size;
        int inaccessibleCount = (int) (totalTiles * INACCESSIBLE_RATIO);
        int marketCount = (int) (totalTiles * MARKET_RATIO);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Tile(TileType.COMMON);
            }
        }

        placeTiles(TileType.INACCESSIBLE, inaccessibleCount);
        placeTiles(TileType.MARKET, marketCount);
    }

    /**
     * Randomly place tiles of a specific type.
     */
    private void placeTiles(TileType type, int count) {
        int placed = 0;
        while (placed < count) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (grid[row][col].getType() == TileType.COMMON) {
                grid[row][col] = new Tile(type);
                placed++;
            }
        }
    }

    /**
     * Place the party at a random accessible location.
     */
    private void placeParty() {
        while (true) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (grid[row][col].isAccessible()) {
                partyRow = row;
                partyCol = col;
                grid[row][col].setHasParty(true);
                break;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getPartyRow() {
        return partyRow;
    }

    public int getPartyCol() {
        return partyCol;
    }

    public Tile getTile(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    public Tile getCurrentTile() {
        return grid[partyRow][partyCol];
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Move the party in a direction.
     */
    public boolean moveParty(int deltaRow, int deltaCol) {
        int newRow = partyRow + deltaRow;
        int newCol = partyCol + deltaCol;

        if (!isValidPosition(newRow, newCol)) {
            System.out.println("Cannot move outside the world!");
            return false;
        }

        if (!grid[newRow][newCol].isAccessible()) {
            System.out.println("Cannot move to inaccessible tile!");
            return false;
        }

        grid[partyRow][partyCol].setHasParty(false);
        partyRow = newRow;
        partyCol = newCol;
        grid[partyRow][partyCol].setHasParty(true);

        return true;
    }

    /**
     * Display the world map.
     */
    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print("  ");
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        // System.out.println("\n Legend: [P] Party [M] Market [X] Blocked [.] Path");
    }
}
