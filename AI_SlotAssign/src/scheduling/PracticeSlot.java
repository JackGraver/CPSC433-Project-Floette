package scheduling;

import enums.Date;

import java.util.ArrayList;

public class PracticeSlot extends Slot {

    private ArrayList<Practice> practices = new ArrayList<>();

    public PracticeSlot(Date date, String time, int max, int min) {
        super(date, time, max, min);
    }

    public String toString() {
        return super.toString() + " Prac Slot";
    }
}
