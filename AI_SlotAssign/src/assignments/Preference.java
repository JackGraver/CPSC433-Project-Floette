package assignments;

import scheduling.Slot;

public class Preference extends Assignment {
    private int preferenceValue;

    public Preference(String gameIdentifier, Slot slot, int preferenceValue) {
        super(gameIdentifier, slot);
        this.preferenceValue = preferenceValue;
    }

    public int getPreferenceValue() {
        return preferenceValue;
    }
}