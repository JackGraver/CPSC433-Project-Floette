package scheduling;

import enums.Sol;
import java.util.ArrayList;

public class ANode {
    private Sol sol;
    private ArrayList<Slot> slots;
    private ArrayList<ANode> children;

    public ANode(ArrayList<Slot> slots) {
        sol = Sol.none;
        this.slots = slots;
        children = new ArrayList<>();
    }

    public Sol getSol() {
        return sol;
    }

    public void setSol(Sol sol) {
        this.sol = sol;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }

    public ArrayList<ANode> getChildren() {
        return children;
    }

    public ANode addChild() {
        children.add(new ANode(slots));
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
            for (int j = 0; j < slots.get(i).getActivities().size(); j++) {
                sb.append(slots.get(i).getActivities().get(j));
                if (j < slots.get(i).getActivities().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            if (i < slots.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        for (ANode n : getChildren()) {
            sb.append("\n\t").append(n.toString());
        }
        return sb.toString();
    }
}