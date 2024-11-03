package assignments;

import scheduling.Activity;

public class NotCompatible extends GameConstraint {
    public NotCompatible(Activity activityOne, Activity activityTwo) {
        super(activityOne, activityTwo);
    }

    public String toString() {
        return getActivityOne() + " not compatible with " + getActivityTwo();
    }
}
