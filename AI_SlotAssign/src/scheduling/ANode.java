package scheduling;

import enums.Sol;

import java.util.ArrayList;

/**
 * And Tree Node recursive data structure
 */
public class ANode {
    /**
     * Current sol value of node
     */
    private Sol sol;
    /**
     * Node data containing n slots of all activity assignments
     * <br>where n is number of slots given from input
     */
    private ArrayList<Slot> slots;
    /**
     * Variable number of children nodes (recursive data structure)
     */
    private ArrayList<ANode> children;

    /**
     * Constructor, does a deep copy of given slots in order to avoid java Object pass-by-reference issues
     *
     * @param slots - slots to be deep copied to create own Node data
     */
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

    /**
     * Eval function to check how for each slot the number of activities assigned compared to minimum needed for slot
     *
     * @return (Slot minimum - activities assigned) for all slots combined
     */
    public int eval_minfilled() {
        int min = 0;
        for (Slot s : slots) {
            min = s.getMin() - s.getActivities().size();
        }
        return min;
    }

    public int eval_pref() {
        int pref = 0;
        for (Slot s : slots) {
            for (Activity a : s.getActivities()) {
                if (a.getPreference() != null) {
                    if (a.getPreference().getSlot() != s) {
                        pref++;
                    }
                }
            }
        }
        return pref;
    }

    public int eval_pair() {
        int pair = 0;
        for (Slot s : slots) {
            for (Activity a : s.getActivities()) {
                if (a.getPair() != null) {
                    if (!s.getActivities().contains(a.getPair().getPairedActivity())) {
                        pair++;
                    }
                }
            }
        }

        return pair;
    }

    public int eval_secdiff() {
        int secdiff = 0;
        for (Slot s : slots) {
            for (Activity a1 : s.getActivities()) {
                for (Activity a2 : s.getActivities()) {
                    if (a2.getDivision() == a1.getDivision()) {
                        secdiff++;
                    }
                }
            }
        }
        return secdiff;
    }

    /**
     * Override toString function
     * <br> Only returns what printTreeHelper returns in-order to make a visually understandable tree representation in console printing
     *
     * @return String representation of tree starting from this node
     */
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

    /**
     * Helper print function, called recursively in order to print all children with correct number of tabs depending on depth
     *
     * @param depth - current depth of node being printed (for depth number of tab chars)
     * @return string representation of tree starting from this node
     */
    private String printTreeHelper(int depth) {
        StringBuilder sb = new StringBuilder();

        // Add indentation for the current depth level and start the node representation
        sb.append("    ".repeat(depth)).append("(");

        // Append the slots and their activities
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

        // Close the node representation and add the solution value
        sb.append("),").append(sol.getSol()).append("\n");

        // Recursively append the children nodes with increased indentation
        for (ANode child : children) {
            sb.append(child.printTreeHelper(depth + 1));
        }

        return sb.toString();
    }
}
