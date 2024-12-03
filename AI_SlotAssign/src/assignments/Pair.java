package assignments;

import scheduling.Activity;

public class Pair {
    private Activity activityOne;
    private Activity activityTwo;

    public Pair(Activity activityOne, Activity activityTwo) {
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
