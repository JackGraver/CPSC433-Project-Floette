package scheduling;

import enums.Date;

public abstract class Slot {
    private Date date;
    private String time;
    private int max;
    private int min;

    public Slot(Date date, String time, int max, int min) {
        this.date = date;
        this.time = time;
        this.max = max;
        this.min = min;
    }

    public String toString() {
        return time + " | " + max + ", " + min;
    }
}
