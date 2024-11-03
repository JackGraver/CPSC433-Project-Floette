package assignments;

import scheduling.Activity;
import scheduling.Slot;

public class Partial extends Assignment {
    public Partial(Activity activity, Slot slot) {
        super(activity, slot);
    }

    @Override
    public String toString() {
        return getActivity() + " pre-assigned to slot " + getSlot();
    }
}