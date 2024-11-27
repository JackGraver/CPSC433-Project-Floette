package assignments;

import scheduling.Activity;

public abstract class GameConstraint {
    private Activity activityOne;
    private Activity activityTwo;

    public GameConstraint(Activity activityOne, Activity activityTwo) {
        this.activityOne = activityOne;
        this.activityTwo = activityTwo;
    }

    public Activity getActivityOne() {
        return activityOne;
    }

    public Activity getActivityTwo() {
        return activityTwo;
    }

}
