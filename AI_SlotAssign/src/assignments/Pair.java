package assignments;

import scheduling.Activity;

public class Pair extends GameConstraint {
    public Pair(Activity activityOne, Activity activityTwo) {
        super(activityOne, activityTwo);
    }

    @Override
    public String toString() {
        return getActivityOne() + " with " + getActivityTwo();
    }
}
