package scheduling;

import enums.Sol;

import java.util.ArrayList;

public class ANode {
    private Sol sol;
    private ArrayList<Slot> slots;
    private ArrayList<ANode> children;

    public ANode(ArrayList<Slot> slots) {
        sol = Sol.none;
        this.slots = new ArrayList<>();
        for (Slot s : slots) {
            this.slots.add(new Slot(s));
        }
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

    public ArrayList<ANode> getChildren() {
        return children;
    }

    public ANode addChild() {
        children.add(new ANode(slots));
        return children.get(children.size() - 1);
    }

    public void addChild(ANode child) {
        children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int eval_minfilled() {
        int min = 0;
        for (Slot s : slots) {
            min = s.getMin() - s.getActivities().size();
        }
        return min;
    }

    public int eval_pref() {
        //use Preference class, need to keep track of if we used
        //one of the preferences in our slot? Either data structure in class
        //or some kind of method (think it would be too much for method)
        return 0;
    }

    public int eval_pair() {
        //same as preference idea implementation
        return 0;
    }

    public int eval_secdiff() {
        //what is this??? where is it defined
        return 0;
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("(");
//        for (int i = 0; i < slots.size(); i++) {
//            sb.append("{");
//            for (int j = 0; j < slots.get(i).getActivities().size(); j++) {
//                sb.append(slots.get(i).getActivities().get(j));
//                if (j < slots.get(i).getActivities().size() - 1) {
//                    sb.append(", ");
//                }
//            }
//            sb.append("}");
//            if (i < slots.size() - 1) {
//                sb.append(", ");
//            }
//        }
//        sb.append("),").append(sol.getSol());
////        for (ANode n : getChildren()) {
////            sb.append("\n\t").append(n);
////        }
//        return sb.toString();
        return printTreeHelper(0);
    }

    private String printTreeHelper(int depth) {
        // Print the current node's slots and activities with indentation
        System.out.print("    ".repeat(depth) + "(");
        for (int i = 0; i < slots.size(); i++) {
            System.out.print("{");
            for (int j = 0; j < slots.get(i).getActivities().size(); j++) {
                System.out.print(slots.get(i).getActivities().get(j));
                if (j < slots.get(i).getActivities().size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("}");
            if (i < slots.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(")");

        // Recursively print the children with increased indentation
        for (ANode child : children) {
            child.printTreeHelper(depth + 1);
        }
        return "";
    }
}
