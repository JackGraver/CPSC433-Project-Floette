package scheduling;

import enums.Days;

import java.util.ArrayList;

public class GameSlot extends Slot {
    private ArrayList<Game> games = new ArrayList<>();

    public GameSlot(Days date, String time, int max, int min) {
        super(date, time, max, min);
    }

    public String toString() {
        return super.toString() + " Game Slot";
    }
}
