package world;

import characters.Hero;
import characters.Monster;

public class ValorMovementStrategy implements MovementStrategy {
    @Override
    public boolean moveHero(Hero hero, int dr, int dc, World world) {
        int newRow = hero.getRow() + dr;
        int newCol = hero.getCol() + dc;

        Tile newTile = world.getTile(newRow, newCol);
        if (newTile == null || !newTile.isAccessible() || newTile.hasHero()) {
            return false;
        }

        // If moving forward (north), check if there's a monster in the CURRENT row blocking the lane
        if (dr == -1) {
            int laneIndex = hero.getCol() / 3;
            int col1 = laneIndex * 3;
            int col2 = laneIndex * 3 + 1;
            
            Tile tile1 = world.getTile(hero.getRow(), col1);
            Tile tile2 = world.getTile(hero.getRow(), col2);
            
            if ((tile1 != null && tile1.hasMonster()) || (tile2 != null && tile2.hasMonster())) {
                System.out.println("Cannot move past monster in your lane! Defeat it first.");
                return false;
            }
        }

        // Remove hero from old tile
        Tile oldTile = world.getTile(hero.getRow(), hero.getCol());
        int heroId = oldTile != null ? oldTile.getHeroId() : 0;

        if (oldTile != null) {
            oldTile.removeHero();
        }

        // Place hero at new tile
        newTile.setHero(hero, heroId);
        hero.setPosition(newRow, newCol);

        // Apply terrain buff for new tile
        hero.applyTerrainBuff(newTile.getType());

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        int newRow = monster.getRow() + 1;
        int col = monster.getCol();

        Tile newTile = world.getTile(newRow, col);
        if (newTile == null || !newTile.isAccessible()) {
            // Check if tile is obstacle - monster will remove it instead of moving
            if (newTile.isObstacle()) {
                System.out.println(monster.getName() + " encounters an obstacle and begins clearing it...");
                newTile.removeObstacle();
                return true;
            }

            return false;
        }

        // Cannot move into hero or monster spaces
        if (newTile.hasHero() || newTile.hasMonster()) {
            return false;
        }

        // If moving forward (south), check if there's a hero in the CURRENT row blocking the lane
        int laneIndex = col / 3;
        int col1 = laneIndex * 3;
        int col2 = laneIndex * 3 + 1;
        
        Tile tile1 = world.getTile(monster.getRow(), col1);
        Tile tile2 = world.getTile(monster.getRow(), col2);
        
        if ((tile1 != null && tile1.hasHero()) || (tile2 != null && tile2.hasHero())) {
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