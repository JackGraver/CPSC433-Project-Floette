package scheduling;

import enums.Days;

public abstract class Slot {
    private Days date;
    private String time;
    private int max;
    private int min;

    public Slot(Days date, String time, int max, int min) {
        this.date = date;
        this.time = time;
        this.max = max;
        this.min = min;
    }

    public Days getDate() {
        return date;
    }

    public void setDate(Days date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String toString() {
        return time + " | " + max + ", " + min;
    }

}
