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

    public boolean isLeaf() {
        return children.isEmpty();
    }
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < slots.size(); i++) {
            sb.append("{");
            for (int j = 0; j < slots.get(i).size(); j++) {
                sb.append(slots.get(i).get(j));
                if (j < slots.get(i).size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            if (i < slots.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
