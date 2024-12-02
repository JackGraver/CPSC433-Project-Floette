package assignments;

import scheduling.Activity;

public class Pair {
    private Activity pairedActivity;

    public Pair(Activity pairedActivity) {
        this.pairedActivity = pairedActivity;
    }

    public Activity getPairedActivity() {
        return pairedActivity;
    }
}
