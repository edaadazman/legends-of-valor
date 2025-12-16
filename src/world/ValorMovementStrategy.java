package world;

import characters.Hero;
import characters.Monster;

/**
 * Movement strategy for Legends of Valor.
 * Heroes and monsters move individually with lane-based blocking rules.
 */

public class ValorMovementStrategy implements MovementStrategy {
    @Override
    public boolean moveHero(Hero hero, int dr, int dc, World world) {
        int newRow = hero.getRow() + dr;
        int newCol = hero.getCol() + dc;

        Tile newTile = world.getTile(newRow, newCol);
        if (newTile == null || !newTile.isAccessible() || newTile.hasHero()) {
            return false;
        }

        // Get hero ID before moving
        Tile oldTile = world.getTile(hero.getRow(), hero.getCol());
        int heroId = oldTile != null ? oldTile.getHeroId() : 0;

        // If moving forward (north), check if there's a monster in the CURRENT row blocking the lane
        if (dr == -1) {
            int laneIndex = hero.getCol() / 3;
            int col1 = laneIndex * 3;
            int col2 = laneIndex * 3 + 1;
            
            Tile tile1 = world.getTile(hero.getRow(), col1);
            Tile tile2 = world.getTile(hero.getRow(), col2);
            
            if (tile1 != null && tile1.hasMonster()) {
                System.out.println("H" + heroId + ": Cannot move past M" + tile1.getMonsterId() + 
                    " (" + tile1.getMonster().getName() + ") in your lane! Defeat it first.");
                return false;
            }
            if (tile2 != null && tile2.hasMonster()) {
                System.out.println("H" + heroId + ": Cannot move past M" + tile2.getMonsterId() + 
                    " (" + tile2.getMonster().getName() + ") in your lane! Defeat it first.");
                return false;
            }
        }

        if (oldTile != null) {
            oldTile.removeHero();
        }

        newTile.setHero(hero, heroId);
        hero.setPosition(newRow, newCol);

        hero.applyTerrainBuff(newTile.getType());

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        int newRow = monster.getRow() + 1;
        int col = monster.getCol();

        // Get monster ID before moving
        Tile oldTile = world.getTile(monster.getRow(), col);
        int monsterId = oldTile != null ? oldTile.getMonsterId() : 0;

        Tile newTile = world.getTile(newRow, col);
        if (newTile == null || !newTile.isAccessible()) {
            // Check if tile is obstacle - monster will remove it instead of moving
            if (newTile != null && newTile.isObstacle()) {
                System.out.println("M" + monsterId + ": " + monster.getName() + 
                    " encounters an obstacle and begins clearing it...");
                newTile.removeObstacle();
                return true;
            }

            return false;
        }

        // Cannot move into another monster's space
        if (newTile.hasMonster()) {
            return false;
        }

        // If moving forward (south), check if there's a hero in the CURRENT row blocking the lane
        int laneIndex = col / 3;
        int col1 = laneIndex * 3;
        int col2 = laneIndex * 3 + 1;
        
        Tile tile1 = world.getTile(monster.getRow(), col1);
        Tile tile2 = world.getTile(monster.getRow(), col2);
        
        if ((tile1 != null && tile1.hasHero()) || (tile2 != null && tile2.hasHero())) {
            // Blocked by hero - don't move
            return false;
        }

        if (oldTile != null) {
            oldTile.removeMonster();
        }
        
        newTile.setMonster(monster, monsterId);
        monster.setPosition(newRow, col);
        return true;
    }
}