package scheduling;

import enums.Days;

public abstract class Slot {
    private Days day;
    private String time;
    private int max;
    private int min;

    public Slot(Days day, String time, int max, int min) {
        this.day = day;
        this.time = time;
        this.max = max;
        this.min = min;
    }

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
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
        return day + " @"+time + " | Max Filled: "+max + ", Min Filed: "+min;
    }

}
