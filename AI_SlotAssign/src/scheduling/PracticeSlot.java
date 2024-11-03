package scheduling;

import enums.Days;

import java.util.ArrayList;

public class PracticeSlot extends Slot {

    private ArrayList<Practice> practices = new ArrayList<>();

    public PracticeSlot(Days date, String time, int max, int min) {
        super(date, time, max, min);
    }

    public String toString() {
        return super.toString() + " Prac Slot";
    }
}
