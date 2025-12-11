package game;

import world.ValorWorld;

public class ValorGameEngine {
    private ValorWorld world;

    public ValorGameEngine() {
        world = new ValorWorld();
    }

    public void start() {
        //TODO
        world.display();
    }
}
