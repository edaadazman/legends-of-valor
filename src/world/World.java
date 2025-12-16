package world;

import characters.Hero;
import characters.Monster;

import java.util.Random;

/**
 * Abstract base Class for the game world as a grid of tiles.
 */
public abstract class World {

    protected Tile[][] grid;
    protected int size;
    protected int partyRow;
    protected int partyCol;
    protected Random random;
    protected MovementStrategy movementStrategy;

    public World(int size) {
        this.size = size;
        this.grid = new Tile[size][size];
        this.random = new Random();
        generate();
    }

    public int getSize() {
        return size;
    }

    protected abstract void generate();

    protected abstract void placeTiles(TileType type, int count);

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public boolean moveHero(Hero hero, int dr, int dc) {
        return movementStrategy.moveHero(hero, dr, dc, this);
    }

    public boolean moveMonster(Monster monster) {
        return movementStrategy.moveMonster(monster, this);
    }

    protected void placeParty() {
        while (true) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (grid[row][col].isAccessible()) {
                partyRow = row;
                partyCol = col;
                grid[row][col].setHasParty(true);
                return;
            }
        }
    }

    public void setPartyPosition(int row, int col) {
        this.partyRow = row;
        this.partyCol = col;
    }

    public Tile getCurrentTile() {
        return grid[partyRow][partyCol];
    }

    public int getPartyRow() { return partyRow; }
    public int getPartyCol() { return partyCol; }

    public boolean isValid(int r, int c) { return r >= 0 && r < size && c >= 0 && c < size; }

    public Tile getTile(int r, int c) { return isValid(r, c) ? grid[r][c] : null; }

    public void display() {
        for (int r = 0; r < size; r++) {
            System.out.print("  ");
            for (int c = 0; c < size; c++) {
                System.out.print(grid[r][c].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}