package game;

/**
* Enum for difficulty levels.
*/
public enum Difficulty {
    EASY(8, "Monsters spawn every 8 rounds"),
    MEDIUM(6, "Monsters spawn every 6 rounds"),
    HARD(4, "Monsters spawn every 4 rounds");

    private final int spawnInterval;
    private final String description;

    Difficulty(int spawnInterval, String description) {
        this.spawnInterval = spawnInterval;
        this.description = description;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }

    public String getDescription() {
        return description;
    }
}
