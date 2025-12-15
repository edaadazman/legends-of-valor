package world;

import characters.Hero;
import characters.Monster;

public class ValorMovementStrategy implements MovementStrategy {
    @Override
    public boolean moveHero(Hero hero, int dr, int dc, World world) {
        int newRow = hero.getRow() + dr;
        int newCol = hero.getCol() + dc;

        Tile newTile = world.getTile(newRow, newCol);
        if (newTile == null || !newTile.isAccessible() || newTile.hasHero()) return false;


        Tile oldTile = world.getTile(hero.getRow(), hero.getCol());
        int heroId = oldTile != null ? oldTile.getHeroId() : 0;

        // If moving forward (north), check if there's a monster in the CURRENT row blocking the lane
        if (dr == -1) { // Moving north (towards enemy nexus)
            int laneIndex = hero.getCol() / 3;
            int col1 = laneIndex * 3;
            int col2 = laneIndex * 3 + 1;
            
            // Check both columns of current lane at CURRENT row
            Tile tile1 = world.getTile(hero.getRow(), col1);
            Tile tile2 = world.getTile(hero.getRow(), col2);
            
            if ((tile1 != null && tile1.hasMonster()) || (tile2 != null && tile2.hasMonster())) {
                System.out.println("Cannot move past monster in your lane! Defeat it first.");
                return false;
            }
        }

        if (oldTile != null) {
            oldTile.removeHero();
        }
        newTile.setHero(hero, heroId);
        hero.setPosition(newRow, newCol);

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        int newRow = monster.getRow() + 1;
        int col = monster.getCol();

        Tile newTile = world.getTile(newRow, col);
        if (newTile == null || !newTile.isAccessible()) {
            return false;
        }

        // Check if tile is obstacle - monster will remove it instead of moving
        if (newTile.isObstacle()) {
            System.out.println(monster.getName() + " encounters an obstacle and begins clearing it...");
            newTile.removeObstacle();
            System.out.println(monster.getName() + " has cleared the obstacle!");
            return true; // Return true - monster spent turn removing obstacle
        }

        // Cannot move into hero or monster spaces
        if (newTile.hasHero() || newTile.hasMonster()) {
            return false;
        }

        // If moving forward (south), check if there's a hero in the CURRENT row blocking the lane
        int laneIndex = col / 3;
        int col1 = laneIndex * 3;
        int col2 = laneIndex * 3 + 1;
        
        // Check both columns of current lane at CURRENT row
        Tile tile1 = world.getTile(monster.getRow(), col1);
        Tile tile2 = world.getTile(monster.getRow(), col2);
        
        if ((tile1 != null && tile1.hasHero()) || (tile2 != null && tile2.hasHero())) {
            // Monster is blocked by hero in current row
            return false;
        }

        Tile oldTile = world.getTile(monster.getRow(), col);
        int monsterId = oldTile != null ? oldTile.getMonsterId() : 0;
        
        oldTile.removeMonster();
        newTile.setMonster(monster, monsterId);
        monster.setPosition(newRow, col);
        return true;
    }
}
