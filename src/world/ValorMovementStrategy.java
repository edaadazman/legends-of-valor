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

        world.getTile(hero.getRow(), hero.getCol()).removeHero();
        newTile.setHero(hero);
        hero.setPosition(newRow, newCol);

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        int newRow = monster.getRow() + 1; // example forward movement
        int col = monster.getCol();

        Tile newTile = world.getTile(newRow, col);
        if (newTile != null && newTile.isAccessible() && !newTile.hasMonster()) {
            world.getTile(monster.getRow(), col).removeMonster();
            newTile.setMonster(monster);
            monster.setPosition(newRow, col);
            return true;
        }

        return false;
    }
}
