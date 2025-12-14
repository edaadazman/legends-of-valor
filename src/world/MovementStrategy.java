package world;

import characters.Hero;
import characters.Monster;

public interface MovementStrategy {
    boolean moveHero(Hero hero, int deltaRow, int deltaCol, World world);
    boolean moveMonster(Monster monster, World world);
}
