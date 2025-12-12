package game;

import characters.Party;
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
        System.out.println("Heroes ready: " + party.size());
        world.display();
    }
}
