package assignments;

import scheduling.Activity;
import scheduling.Slot;

public class Preference extends Assignment {
    private int preferenceValue;

    public Preference(Activity activity, Slot slot, int preferenceValue) {
        super(activity, slot);
        this.preferenceValue = preferenceValue;
    }

    public int getPreferenceValue() {
        return preferenceValue;
    }

    @Override
    public String toString() {
        return getActivity().getIdentifier() + " prefers " + getSlot() + " with preference: " + preferenceValue;
    }
}