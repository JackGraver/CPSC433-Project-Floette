package scheduling;

import enums.Sol;
import java.util.ArrayList;

public class ANode {
    private Sol sol;
    private ArrayList<ArrayList<Activity>> slots;
    private ArrayList<ANode> children;

    public ANode(int numSlots) {
        sol = Sol.none;
        slots = new ArrayList<>();
        for(int i = 0; i < numSlots; i++) {
            slots.add(new ArrayList<>());
        }
        children = new ArrayList<>();
    }

    public Sol getSol() {
        return sol;
    }

    public void setSol(Sol sol) {
        this.sol = sol;
    }

    public ArrayList<ArrayList<Activity>> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<ArrayList<Activity>> slots) {
        this.slots = slots;
    }

    public ArrayList<ANode> getChildren() {
        return children;
    }

    public ANode addChild() {
        children.add(new ANode(slots.size()));
        return children.get(children.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("(");

        for(ArrayList<Activity> sL : slots) {
            s.append("{");
            for(Activity a : sL) {
                s.append(a);
            }
            s.append("}, ");
        }
        s.deleteCharAt(s.length()-1);
        s.deleteCharAt(s.length()-1);
        s.append("),");
        s.append(sol.getSol());

        return s.toString();
    }
}
