package game;

import characters.Hero;
import characters.Party;
import util.InputHelper;
import world.Tile;
import world.ValorWorld;

public class ValorGameEngine {
    private final ValorWorld world;
    private final Party party;

    public ValorGameEngine() {
        this(new Party());
    }

    public ValorGameEngine(Party party) {
        this.party = party;
        this.world = new ValorWorld();
    }

    public void start() {
        System.out.println("\n===========================================");
        System.out.println("  LEGENDS OF VALOR");
        System.out.println("  Three lanes await—defend your nexus!");
        System.out.println("===========================================\n");

        placeHeroesAtBottomNexus();

        boolean running = true;
        while (running) {
            world.display();

            // Heroes move in order H1 -> H3 each round
            int heroesToPlay = Math.min(3, party.size());
            for (int heroIdx = 0; heroIdx < heroesToPlay && running; heroIdx++) {
                Hero hero = party.getHero(heroIdx);

                System.out.println("\n--- H" + (heroIdx + 1) + " Turn (" + hero.getName() + ") ---");
                System.out.println("W/A/S/D - Move");
                System.out.println("I - Info");
                System.out.println("Q - Quit");

                boolean turnComplete = false;
                while (!turnComplete && running) {
                    char cmd = Character.toLowerCase(InputHelper.readChar("Command: "));
                    switch (cmd) {
                        case 'w':
                            turnComplete = attemptMove(hero, -1, 0);
                            break;
                        case 's':
                            turnComplete = attemptMove(hero, 1, 0);
                            break;
                        case 'a':
                            turnComplete = attemptMove(hero, 0, -1);
                            break;
                        case 'd':
                            turnComplete = attemptMove(hero, 0, 1);
                            break;
                        case 'i':
                            hero.displayStats();
                            break;
                        case 'q':
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                }
            }
        }
    }

    /** Attempt a hero move; returns true only when the move succeeds. */
    private boolean attemptMove(Hero hero, int dr, int dc) {
        boolean ok = world.moveHero(hero, dr, dc);
        if (!ok) {
            System.out.println("Move blocked.");
        }
        return ok;
    }

    /** Place the three heroes on the bottom nexus (one per lane) and label them H1-H3. */
    private void placeHeroesAtBottomNexus() {
        if (party.size() == 0) {
            System.out.println("No heroes in party.");
            return;
        }

        // One hero per lane (cols 0/3/6 are lane anchors; cols 2 and 5 are walls)
        int[] laneCols = { 0, 3, 6 };
        int row = world.getSize() - 1; // bottom Nexus row in ValorWorld

        int heroesToPlace = Math.min(Math.min(party.size(), laneCols.length), 3);
        for (int i = 0; i < heroesToPlace; i++) {
            Hero hero = party.getHero(i);
            int col = laneCols[i];

            Tile tile = world.getTile(row, col);
            if (tile == null || !tile.isAccessible() || tile.hasHero()) {
                System.out.println("Could not place " + hero.getName() + " at (" + row + "," + col + ").");
                continue;
            }

            tile.setHero(hero, i + 1);
            hero.setPosition(row, col);
        }
    }
}
