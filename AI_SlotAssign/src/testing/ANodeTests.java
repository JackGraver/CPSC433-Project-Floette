package testing;

import enums.Days;
import enums.SlotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scheduling.ANode;
import scheduling.Game;
import scheduling.Slot;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ANodeTests {

    private ANode node;

    @BeforeEach
    void setup() {
        ArrayList<Slot> slots = new ArrayList<>();
        slots.add(new Slot(SlotType.Game, Days.MO, "8:00", 6, 5));
        node = new ANode(slots);
        node.getSlots().get(0).getActivities().add(new Game("Game1"));
        node.getSlots().get(0).getActivities().add(new Game("Game2"));
        node.getSlots().get(0).getActivities().add(new Game("Game3"));
    }

    @Test
    public void testMinFilled() {
        int res = node.eval_minfilled(1, 1);
        assertEquals(2, res);
    }
}
