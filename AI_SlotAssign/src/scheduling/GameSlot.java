package scheduling;

import enums.Days;
import enums.SlotType;

import java.util.ArrayList;

public class GameSlot extends Slot {
    private ArrayList<Game> games = new ArrayList<>();

    public GameSlot(Days date, String time, int max, int min) {
        super(SlotType.Game, date, time, max, min);
    }

    public String toString() {
        return "[Game Slot] " + super.toString();
    }
}
