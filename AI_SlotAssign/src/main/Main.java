package main;

import assignments.*;
import enums.Days;
import enums.SlotType;
import enums.Sol;
import scheduling.ANode;
import scheduling.Activity;
import scheduling.Game;
import scheduling.Practice;
import scheduling.Slot;
import utility.Data;
import utility.Setup;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static Data data;
    private static ArrayList<ANode> nodeQueue;
    private static ANode currentBest = null;
    private static int currentBestEval = Integer.MAX_VALUE;

    public static void main(String[] args) throws Exception {
        try {
            data = Setup.setup(args); // load data from given file
            System.out.println(data.getActivities().size());
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file! Exiting Program.");
            System.exit(0);
        }

        nodeQueue = new ArrayList<>(); // create queue for going through expansion of leaves

        ANode root = new ANode(data.getSlots()); // initialize root node
        nodeQueue.add(root); // add to queue (to be expanded first)

        while (!nodeQueue.isEmpty()) { // while there are nodes to expand (not sol =
            if (System.in.available() > 0) {
                System.out.println("Found break key, exiting main loop");
                break;
            }

            ANode expansion_node = f_leaf(); // get next node to expand (f_leaf)
            Activity placement_activity = f_trans(expansion_node); // get next activity to place in expanded node (f_trans)

            if (div(expansion_node, placement_activity) == 0 || placement_activity == null) {
                expansion_node.setSol(Sol.yes);
                if (currentBest == null) {
                    currentBest = expansion_node;
                    currentBestEval = expansion_node.getEval();
                } else if (expansion_node.numberActivitiesAssigned() >= currentBest.numberActivitiesAssigned() && expansion_node.getEval() < currentBestEval) {
                    currentBest = expansion_node;
                    currentBestEval = expansion_node.getEval();
                }
            }

            data.removeAllActivity(placement_activity);
            nodeQueue.remove(expansion_node);
        }
        printResults();
    }

    /**
     * Handles expansion of a node given an activity to assign
     *
     * @param node - node being expanded
     * @param curr - activity being assigned to slots
     */
    private static int div(ANode node, Activity curr) {
        int branches = 0;
        ArrayList<Integer> checkedSlots = new ArrayList<>(); // make sure we don't choose the same slot for each expansion leaf (could use better solution)

        // loop through number of slots (n) to potentially create n children nodes
        for (int i = 0; i < node.getSlots().size(); i++) {
            ANode child = new ANode(node.getSlots());
            for (Slot slot : child.getSlots()) {
                if (!checkedSlots.contains(child.getSlots().indexOf(slot))) {
                    // check if it's the correct type of slot (Game Slot for Games, Prac slot for
                    // Practices)
                    if (slot.getType() == SlotType.Game && curr instanceof Game) { // Game
                        checkedSlots.add(child.getSlots().indexOf(slot));
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            branches++;
                            break;
                        }
                    } else if (slot.getType() == SlotType.Practice && curr instanceof Practice) { // Prac
                        checkedSlots.add(child.getSlots().indexOf(slot));
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            branches++;
                            break;
                        }
                    }
                }
            }
        }
        return branches;
    }

    /**
     * Performs the assignment of an activity to a given slot for a new child node
     * of the current node being expanded
     * <br>
     * -Also does hard constraint of adding activity on Monday to equivalent slot on
     * Wednesday and Friday, similarly with Tuesday to equivalent Thursday slot
     *
     * @param parent - parent being expanded
     * @param child  - new child node of parent
     * @param slot   - slot of child activity is being added to
     * @param curr   - activity being added
     * @return if assignment was successful
     */
    private static boolean assignActivityToSlot(ANode parent, ANode child, Slot slot, Activity curr) {
        if (noHardConstraintViolations(slot, curr, child)) {
            slot.addActivity(curr);
            if (slot.getDay() == Days.MO) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.WE && s.getStartTime().equals(slot.getStartTime())) {
                        if (curr instanceof Game && s.isGame()) {
                            s.addActivity(curr);
                        } else if (curr instanceof Practice && !s.isGame()) {
                            s.addActivity(curr);
                        }
                    } else if (s.getDay() == Days.FR && s.getStartTime().equals(slot.getStartTime())) {
                        if (curr instanceof Game && s.isGame()) {
                            s.addActivity(curr);
                        } else if (curr instanceof Practice && !s.isGame()) {
                            s.addActivity(curr);
                        }
                    }
                }
            } else if (slot.getDay() == Days.TU) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.TR && s.getStartTime().equals(slot.getStartTime())) {
                        s.addActivity(curr);
                    }
                }
            }
            parent.addChild(child);
            child.updateEval(data.getPen_gamemin(), data.getPen_practicemin(), data.getW_minfilled(),
                    data.getW_pref(), data.getPen_notpaired(), data.getW_pair(), data.getPen_section(), data.getW_secdiff());
            nodeQueue.add(0, child);
            return true;
        }
        return false;
    }

    private static boolean noHardConstraintViolations(Slot slot, Activity curr, ANode node) {
        if (slot.isFull()) { // 1&2) Not more than gamemax(s)/practicemax(s) activities assigned to each slot
            return false; // violates constraint
        }

        // 4) not compatible activities in slot
        for (NotCompatible nc : data.getNotCompatibles()) { // check all not compatibles
            if (nc.getActivityOne() == curr) { // if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityTwo())) { // and the other activity already in slot
                    return false; // violates constraint
                }
            } else if (nc.getActivityTwo() == curr) { // if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityOne())) { // and the other activity already in slot
                    return false; // violates constraint
                }
            }
        }
        // 5) partial assignments of activities to slot
        for (Partial p : data.getPartials()) {
            if (p.getActivity() == curr) { // if there is a partial assignment for this activity
                if (p.getSlot().getID() != slot.getID()) { // not the correct slot for partial assignment
                    return false; // violates constraint
                }
            }
        }
        // 6) unwanted assignments of activity to slot
        for (Unwanted u : data.getUnwanteds()) {
            if (u.getActivity() == curr) { // exists an unwanted assignment containing this activity
                if (u.getSlot().getID() == slot.getID()) {
                    return false; // violates constraint
                }
            }
        }

        if (String.valueOf(curr.getDivision()).startsWith("9")) {
            if (!slot.isEveningSlot()) {
                return false;
            }
        }

        if (curr instanceof Game && (curr.getAgeGroup().startsWith("U15")
                || curr.getAgeGroup().startsWith("U16")
                || curr.getAgeGroup().startsWith("U17")
                || curr.getAgeGroup().startsWith("U19"))) {
            for (Activity a : slot.getActivities()) {
                if (a.getAgeGroup().startsWith("U15")
                        || curr.getAgeGroup().startsWith("U16")
                        || curr.getAgeGroup().startsWith("U17")
                        || curr.getAgeGroup().startsWith("U19")) {
                    return false;
                }
            }
        }

        if (curr.getFullIdentifier().startsWith("S-")) {
            if ((slot.getStartTime() != LocalTime.of(18, 0))) {
                if (slot.getDay() != Days.TU || slot.getDay() != Days.TR) {
                    return false;
                }
            }
        }

        for (Slot s : node.getSlots()) {
            if (s.getStartTime() == slot.getStartTime() && s.getDay() == slot.getDay() && s != slot) {
                for (Activity a : s.getActivities()) {
                    if (a.getDivision() == curr.getDivision()) {
                        return false;
                    }
                }
            }
        }

        if (curr instanceof Game
                && slot.getDay() == Days.TU
                && slot.getStartTime() == LocalTime.of(11, 0)) {
            return false;
        }

        for (Activity a : slot.getActivities()) {
            if (a instanceof Game && curr instanceof Practice) {
                if (curr == ((Game) a).getAssociatedPractice()) {
                    return false;
                }
            } else if (a instanceof Practice && curr instanceof Game) {
                if (a == ((Game) curr).getAssociatedPractice()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Defines what leaves will be chosen to be explored (expanded by div)
     * <br>
     * - Chooses leaf at the largest depth
     * <br>
     * - In case of ties for largest depth, we choose the node such that
     * Eval(assign) is minimized
     *
     * @return ANode - node to be expanded
     */
    private static ANode f_leaf() {
        ANode choose = null;
        int chooseEval = Integer.MAX_VALUE;
        for (ANode n : nodeQueue) {
            if (n.isLeaf()) {
                choose = n;
                break;
            } else {
                // is leaf node
                if (choose != null) { // not only leaf node
                    int tempEval = n.getEval();
                    if (tempEval < chooseEval) {
                        choose = n;
                        chooseEval = tempEval;
                    }
                } else {
                    choose = n;
                    chooseEval = n.getEval();
                }
            }
        }
        return choose;
    }

    /**
     * Defines what activity will be chosen to be explored (assigned to slot)
     * - Chooses based off different hard constraints and other checks
     * - If no hard constraint violations, chooses the first activity yet to be
     * assigned
     *
     * @return Activity - The Game or Practice being assigned to slot(s)
     */
    public static Activity f_trans(ANode node) {
        if (!data.getPartials().isEmpty()) { // 3.1 & 3.2
            for (Partial p : data.getPartials()) {
                if (notAlreadyAssigned(node, p.getActivity())) {
                    return p.getActivity();
                }
            }
        }

        if (!data.getNotCompatibles().isEmpty()) { // 3.5 & 3.6
            for (NotCompatible nc : data.getNotCompatibles()) {
                if (nc.aOneAssigned) {
                    if (notAlreadyAssigned(node, nc.getActivityTwo())) {
                        return nc.getActivityTwo();
                    }
                }
                if (nc.aTwoAssigned) {
                    if (notAlreadyAssigned(node, nc.getActivityOne())) {
                        return nc.getActivityOne();
                    }
                }
            }
        }

        if (!data.getUnwanteds().isEmpty()) { // 3.7 & 3.8
            for (Unwanted u : data.getUnwanteds()) {
                if (notAlreadyAssigned(node, u.getActivity())) {
                    return u.getActivity();
                }
            }
        }

        for (Evening e : data.getEveningActivities()) {
            if (notAlreadyAssigned(node, e.getActivity())) {
                return e.getActivity();
            }
        }

        for (Pair p : data.getPairs()) {
            if (notAlreadyAssigned(node, p.getActivityOne())) {
                return p.getActivityOne();
            } else if (notAlreadyAssigned(node, p.getActivityTwo())) {
                return p.getActivityTwo();
            }
        }

        for (Activity ac : data.getActivities()) {
            if (notAlreadyAssigned(node, ac)) {
                return ac;
            }
        }

        return null;
    }

    private static boolean notAlreadyAssigned(ANode expansion_node, Activity placement_activity) {
        for (Slot s : expansion_node.getSlots()) {
            for (Activity a : s.getActivities()) {
                if (a == placement_activity) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printResults() {
        if (currentBest != null) {
            System.out.println(printOutput(currentBest));
            if (currentBest.numberActivitiesAssigned() < data.getActivities().size()) {
                System.out.println("Not all activities assigned.");
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    private static String printOutput(ANode node) {
        StringBuilder out = new StringBuilder();
        out.append("Eval-value: ").append(node.getEval()).append("\n");

        TreeMap<String, String> sorted = new TreeMap<>();

        for (Slot s : node.getSlots()) {
            for (Activity a : s.getActivities()) {
                sorted.put(a.getFullIdentifier(), s.getDay().getShortCode() + ", " + s.getStartTime());
            }
        }
        int maxKeyLength = sorted.keySet().stream().mapToInt(String::length).max().orElse(0);

        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            out.append(String.format("%-" + maxKeyLength + "s : %s%n", key, value));
        }

        return out.toString();
    }
}