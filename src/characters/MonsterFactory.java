package characters;

import data.GameDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating monsters for battles.
 */
public class MonsterFactory {

    /**
     * Create a group of monsters scaled to hero party level.
     */
    public static List<Monster> createMonsterGroup(int partySize, int heroLevel) {
        List<Monster> monsters = new ArrayList<>();
        GameDatabase db = GameDatabase.getInstance();

        for (int i = 0; i < partySize; i++) {
            Monster template = db.getRandomMonster();
            if (template != null) {
                Monster monster = new Monster(
                        template.getName(),
                        heroLevel,
                        template.getMonsterType(),
                        template.getBaseDamage(),
                        template.getDefense(),
                        (int) (template.getDodgeChance() * 100));
                monsters.add(monster);
            }
        }

        return monsters;
    }
}
