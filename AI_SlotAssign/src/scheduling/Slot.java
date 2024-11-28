package scheduling;

import enums.Days;
import enums.SlotType;

import java.util.ArrayList;

public class Slot {
    private static int SLOT_IDS = 0;
    private int id;
    private SlotType type;
    private Days day;
    private String time;
    private int max;
    private int min;
    private ArrayList<Activity> activities;

    public Slot(SlotType type, Days day, String time, int max, int min) {
        this.id = ++SLOT_IDS;
        this.type = type;
        this.day = day;
        this.time = time;
        this.max = max;
        this.min = min;
        activities = new ArrayList<>();
    }

    public Slot(Slot other) {
        this.id = other.id;
        this.type = other.type;
        this.day = other.day;
        this.time = other.time;
        this.max = other.max;
        this.min = other.min;

        this.activities = new ArrayList<>();
        this.activities.addAll(other.activities);
    }

    public int getID() {
        return id;
    }

    public SlotType getType() {
        return type;
    }

    public void setType(SlotType type) {
        this.type = type;
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

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public boolean isFull() {
        return activities.size() == max;
    }

    public String toString() {
        return "[" + id + "] (" + type + ") " + day + " @" + time + " | Max Filled: " + max + ", Min Filed: " + min;
    }

}
