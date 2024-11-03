package scheduling;

import enums.Date;

import java.sql.Array;
import java.util.ArrayList;

public class GameSlot extends Slot {
    private ArrayList<Game> games = new ArrayList<>();

    public GameSlot(Date date, String time, int max, int min) {
        super(date, time, max, min);
    }

    public String toString() {
        return super.toString() + " Game Slot";
    }
}
