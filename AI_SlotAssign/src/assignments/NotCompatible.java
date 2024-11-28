package assignments;

import scheduling.Activity;

public class NotCompatible extends GameConstraint {
    public boolean aOneAssigned = false;
    public boolean aTwoAssigned = false;

    public NotCompatible(Activity activityOne, Activity activityTwo) {
        super(activityOne, activityTwo);
    }

    public String toString() {
        return getActivityOne() + " with " + getActivityTwo();
    }
}
