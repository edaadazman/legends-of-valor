package world;

import characters.Hero;
import characters.Monster;

public class ValorMovementStrategy implements MovementStrategy {
    @Override
    public boolean moveHero(Hero hero, int dr, int dc, World world) {
        int newRow = hero.getRow() + dr;
        int newCol = hero.getCol() + dc;

        Tile newTile = world.getTile(newRow, newCol);
        if (newTile == null || !newTile.isAccessible() || newTile.hasHero())
            return false;

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
        int newRow = monster.getRow() + 1;
        int col = monster.getCol();

        Tile currentTile = world.getTile(monster.getRow(), col);
        Tile newTile = world.getTile(newRow, col);

        if (newTile == null || !newTile.isAccessible()) {
            return false;
        }

        if (newTile.hasMonster()) {
            return false;
        }

        if (currentTile != null && currentTile.hasHero()) {
            return false;
        }

        if (currentTile != null) {
            currentTile.removeMonster();
        }
        newTile.setMonster(monster);
        monster.setPosition(newRow, col);
        return true;
    }
}
