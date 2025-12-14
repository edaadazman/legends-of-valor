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

        if (oldTile != null) {
            oldTile.removeHero();
        }
        newTile.setHero(hero, heroId);
        hero.setPosition(newRow, newCol);

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        int newRow = monster.getRow() + 1; // example forward movement
        int col = monster.getCol();

        Tile newTile = world.getTile(newRow, col);
        if (newTile != null && newTile.isAccessible() && !newTile.hasMonster()) {
            Tile oldTile = world.getTile(monster.getRow(), col);
            int monsterId = oldTile != null ? oldTile.getMonsterId() : 0;
            
            oldTile.removeMonster();
            newTile.setMonster(monster, monsterId);
            monster.setPosition(newRow, col);
            return true;
        }

        return false;
    }
}
