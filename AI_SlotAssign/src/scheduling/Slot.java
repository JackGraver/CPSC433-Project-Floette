package scheduling;

import enums.Days;
import enums.SlotType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Object representing slot
 */
public class Slot {
    /**
     * Unique static ID variable for all Slot objects
     */
    private static int SLOT_IDS = 0;
    /**
     * Unique ID for object
     */
    private int id;
    /**
     * What kind of activities this slot holds
     */
    private SlotType type;
    /**
     * Day of the week for the slot
     */
    private Days day;
    /**
     * Start Time of the slot
     */
    private LocalTime startTime;
    /**
     * End time of the slot
     */
    private LocalTime endTime;
    /**
     * Max number of activities that can be assigned to slot
     */
    private int max;
    /**
     * Minimum number of activities that must be assigned to slot (to avoid
     * penalties)
     */
    private int min;
    /**
     * List of activities assigned to slot
     */
    private ArrayList<Activity> activities;

    /**
     * If slot is evening slot (18:00 and later)
     */
    private boolean eveningSlot;

    private boolean checked;
    /**
     * Slot Constructor
     * <br>
     * - Initializes activites to empty array
     *
     * @param type type
     * @param day  day
     * @param time time
     * @param max  max
     * @param min  min
     */
    public Slot(SlotType type, Days day, String time, int max, int min) {
        this.id = ++SLOT_IDS;
        this.type = type;
        this.day = day;
        parseTime(time);
        this.max = max;
        this.min = min;
        activities = new ArrayList<>();
        eveningSlot = startTime.isAfter(LocalTime.of(18, 0));
        this.checked = false;
    }

    /**
     * Parse from String xx:xx-yy:yy into LocalTime start and end time variables
     *
     * @param time String representation of time
     */
    private void parseTime(String time) {
        String[] split = time.replace(" ", "").split(":");
        this.startTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        if (day == Days.MO || day == Days.WE || day == Days.FR) {
            this.endTime = startTime.plusHours(1);
        } else if (day == Days.TU || day == Days.TR) {
            this.endTime = startTime.plusHours(1).plusMinutes(30);
        }
    }

    /**
     * Deep copy Slot Constructor
     *
     * @param other Slot to copy
     */
    public Slot(Slot other) {
        this.id = other.id;
        this.type = other.type;
        this.day = other.day;
        this.startTime = other.startTime;
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

    public boolean isGame() {
        return type == SlotType.Game;
    }

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
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

    public boolean isEveningSlot() {
        return startTime.isAfter(LocalTime.of(17, 59));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked() {
        checked = true;
    }
    public String toString() {
        return "[" + id + "] (" + type + ") " + day + " @" + startTime + "-" + endTime + " | Max Filled: " + max
                + ", Min Filed: " + min;
    }
}
