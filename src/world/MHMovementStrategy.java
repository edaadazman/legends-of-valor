package world;

import characters.Hero;
import characters.Monster;

public class MHMovementStrategy implements MovementStrategy {
    @Override
    public boolean moveHero(Hero hero, int deltaRow, int deltaCol, World world) {
        int newRow = world.getPartyRow() + deltaRow;
        int newCol = world.getPartyCol() + deltaCol;

        Tile newTile = world.getTile(newRow, newCol);
        if (newTile == null || !newTile.isAccessible()) return false;

        // Move Party
        world.getTile(world.getPartyRow(), world.getPartyCol()).setHasParty(false);
        newTile.setHasParty(true);
        world.setPartyPosition(newRow, newCol);

        return true;
    }

    @Override
    public boolean moveMonster(Monster monster, World world) {
        // Monsters dont move
        return false;
    }
}
