package scheduling;

public class ActivityAssignment {
    private Activity activity;
    private Slot slot;

    // Constructor
    public ActivityAssignment(Activity activity, Slot slot) {
        this.activity = activity;
        this.slot = slot;
    }

    public ActivityAssignment(){}

    // Getters
    public Activity getActivity() {
        return activity;
    }

    public Slot getSlot() {
        return slot;
    }

    // Setters
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    // toString method
    @Override
    public String toString() {
        return "Assignment{" +
                "activity=" + activity +
                ", slot=" + slot +
                '}';
    }
}
