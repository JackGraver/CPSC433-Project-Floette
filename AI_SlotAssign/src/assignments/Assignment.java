package assignments;

import scheduling.Slot;

public class Assignment {
    private String gameIdentifier;
    private Slot slot;

    public Assignment(String gameIdentifier, Slot slot) {
        this.gameIdentifier = gameIdentifier;
        this.slot = slot;
    }

    public String getGameIdentifier() {
        return gameIdentifier;
    }

    public Slot getSlot() {
        return slot;
    }
}
