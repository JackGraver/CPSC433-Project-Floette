package assignments;

import scheduling.Activity;
import scheduling.Slot;

public class Preference {
    private Slot slot;
    private int preferenceValue;

    public Preference(Slot slot, int preferenceValue) {
        this.slot = slot;
        this.preferenceValue = preferenceValue;
    }

    public Slot getSlot() {
        return slot;
    }

    public int getPreferenceValue() {
        return preferenceValue;
    }

    @Override
    public String toString() {
        return "preference value: " + preferenceValue;
    }
}