package world;

import characters.Hero;
import characters.Monster;

/**
 * Strategy interface for character movement.
 * Allows different game modes to define custom movement rules.
 */

public interface MovementStrategy {
    boolean moveHero(Hero hero, int deltaRow, int deltaCol, World world);
    boolean moveMonster(Monster monster, World world);
}
