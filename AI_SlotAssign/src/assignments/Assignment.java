package assignments;

import scheduling.Activity;
import scheduling.Slot;

public abstract class Assignment {
    private Activity activity;
    private Slot slot;

    public Assignment(Activity activity, Slot slot) {
        this.activity = activity;
        this.slot = slot;
    }

    public Activity getActivity() {
        return activity;
    }

    public Slot getSlot() {
        return slot;
    }
}
