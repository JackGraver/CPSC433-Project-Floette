package scheduling;

import assignments.Preference;
import enums.Sol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * And Tree Node recursive data structure
 */
public class ANode {
    /**
     * Current sol value of node
     */
    private Sol sol;

    private int eval;

    private boolean isLeaf;
    /**
     * Node data containing n slots of all activity assignments
     * <br>
     * where n is number of slots given from input
     */
    private final ArrayList<Slot> slots;
    /**
     * Variable number of children nodes (recursive data structure)
     */
    private final ArrayList<ANode> children;

    /**
     * Constructor, does a deep copy of given slots in order to avoid java Object
     * pass-by-reference issues
     *
     * @param slots - slots to be deep copied to create own Node data
     */
    public ANode(ArrayList<Slot> slots) {
        sol = Sol.none;
        this.eval = 0;
        isLeaf = true;
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

    public void addChild(ANode child) {
        children.add(child);
        isLeaf = false;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setNotLeaf() {
        isLeaf = false;
    }

    public int numberActivitiesAssigned() {
        int n = 0;
        for (Slot s : slots) {
            n += s.getActivities().size();
        }
        return n;
    }


    public int getEval() {
        return this.eval;
    }

    public void updateEval(int pen_gamemin, int pen_practicemin, int w_minfilled, int w_pref, int pen_notpaired, int w_pair, int pen_section, int w_secdiff) {
        this.eval = (eval_minfilled(pen_gamemin, pen_practicemin) * w_minfilled)
                + (eval_pref(1) * w_pref)
                + (eval_pair(pen_notpaired) * w_pair)
                + (eval_secdiff(pen_section) * w_secdiff);
    }

    /**
     * Eval function to check how for each slot the number of activities assigned
     * compared to minimum needed for slot
     *
     * @return (Slot minimum - activities assigned) for all slots combined
     */
    public int eval_minfilled(int gamePenalty, int pracPenalty) {
        int min = 0;
        for (Slot s : slots) {
            if ((s.getMin() - s.getActivities().size()) > min) {
                min = (s.getMin() - s.getActivities().size());
            }
        }
        return min;
    }

    public int eval_pref(int penalty) {
        int pref = 0;
        for (Slot s : slots) {
            for (Activity a : s.getActivities()) {
                if (!a.getPreference().isEmpty()) {
                    for (Preference p : a.getPreference()) {
                        if (p.getSlot().getDay() != s.getDay() || p.getSlot().getStartTime() != s.getStartTime()) {
                            pref += p.getPreferenceValue();
                        }
                    }
                }
            }
        }
        return pref;
    }

    public int eval_pair(int penalty) {
        int pair = 0;
        for (Slot s : slots) {
            for (Activity a : s.getActivities()) {
                if (!a.getPairs().isEmpty()) {
                    for (Activity pa : a.getPairs()) {
                        if (!s.getActivities().contains(pa)) {
                            pair++;
                        }
                    }
                }
            }
        }
        return (pair * penalty) / 2;
    }

    public int eval_secdiff(int penalty) {
        int secdiff = 0;

        for (Slot s : slots) {
            Set<String> ageGroups = new HashSet<>();
            for (Activity activity : s.getActivities()) {
                if (!ageGroups.add(activity.getAgeGroup())) {
                    secdiff++;
                    break;
                }
            }

        }
        return (secdiff * penalty) / 2;
    }

    public String printSolo() {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    /**
     * Override toString function
     * <br>
     * Only returns what printTreeHelper returns in-order to make a visually
     * understandable tree representation in console printing
     *
     * @return String representation of tree starting from this node
     */
    @Override
    public String toString() {
        return printTreeHelper(0);
    }

    /**
     * Helper print function, called recursively in order to print all children with
     * correct number of tabs depending on depth
     *
     * @param depth - current depth of node being printed (for depth number of tab
     *              chars)
     * @return string representation of tree starting from this node
     */
    private String printTreeHelper(int depth) {
        StringBuilder sb = new StringBuilder();

        sb.append("    ".repeat(depth)).append("(");

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

        sb.append("),").append(sol.getSol()).append("\n");

        for (ANode child : children) {
            sb.append(child.printTreeHelper(depth + 1));
        }

        return sb.toString();
    }
}
