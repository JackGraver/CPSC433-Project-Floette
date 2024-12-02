package assignments;

import scheduling.Activity;
import scheduling.Slot;

public class Unwanted extends Assignment {
    public Unwanted(Activity activity, Slot slot) {
        super(activity, slot);
    }

    @Override
    public String toString() {
        return getActivity().getFullIdentifier() + " does not want slot " + getSlot().toString();
    }
}