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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {

    public static Data data;
    private static ArrayList<ANode> nodeQueue;
    private static ArrayList<ANode> completedNodes;

    public static void main(String[] args) throws Exception {
        try {
            data = Setup.setup(args); // load data from given file
            System.out.println(data.getPairs().size());
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file! Exiting Program.");
            System.exit(0);
        }

        nodeQueue = new ArrayList<>(); // create queue for going through expansion of leaves
        completedNodes = new ArrayList<>();

        ANode root = new ANode(data.getSlots()); // initialize root node
        nodeQueue.add(root); // add to queue (to be expanded first)

        // is a for loop for testing
        while (!nodeQueue.isEmpty()) { // while there are nodes to expand (not sol = yes)
            System.out.println("---Main Loop---");
            System.out.println(root);
            for (ANode n : nodeQueue) {
                System.out.println("\t" + n.printSolo());
            }
            ANode expansion_node = f_leaf(); // get next node to expand (f_leaf)
            System.out.println("\tExpanding Node: " + expansion_node);
            Activity placement_activity = f_trans(expansion_node); // get next activity to place in expanded node (f_trans)
            System.out.println("\tPlacing activity: " + placement_activity);

            if (div(expansion_node, placement_activity) == 0) { // handle expansion
                expansion_node.setSol(Sol.yes);
            }
            if (placement_activity == null) {
                System.out.println("No more activities to assign. Soling " + expansion_node + " to yes");
                expansion_node.setSol(Sol.yes);
            }

            data.removeAllActivity(placement_activity);
            nodeQueue.remove(expansion_node);
            completedNodes.add(expansion_node);
        }
        System.out.println(root);
        printResults();
    }

    /**
     * Handles expansion of a node given an activity to assign
     *
     * @param node - node being expanded
     * @param curr - activity being assigned to slots
     */
    private static int div(ANode node, Activity curr) {
        System.out.println("---Div---");
        int branches = 0;
        ArrayList<Integer> assigned = new ArrayList<>(); // make sure we don't choose the same slot for each expansion
        // leaf (could use better solution)

        // loop through number of slots (n) to potentially create n children nodes
        for (int i = 0; i < node.getSlots().size(); i++) {
            System.out.println("\tTop div loop");
            ANode child = new ANode(node.getSlots());
            for (Slot slot : child.getSlots()) {
                System.out.println("\t\tInside div loop");
                if (!assigned.contains(child.getSlots().indexOf(slot))) {
                    // check if it's the correct type of slot (Game Slot for Games, Prac slot for
                    // Practices)
                    if (slot.getType() == SlotType.Game && curr instanceof Game) { // Game
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            assigned.add(child.getSlots().indexOf(slot));
                            branches++;
                            break;
                        }
                    } else if (slot.getType() == SlotType.Practice && curr instanceof Practice) { // Prac
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            assigned.add(child.getSlots().indexOf(slot));
                            branches++;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("Created " + branches + " branches");
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
     * \
     */
    private static boolean assignActivityToSlot(ANode parent, ANode child, Slot slot, Activity curr) {
        if (noHardConstraintViolations(slot, curr, child)) {
            System.out.println("\t\tAssign " + curr + " to " + slot);
            slot.addActivity(curr);
            if (slot.getDay() == Days.MO) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.WE && s.getStartTime().equals(slot.getStartTime())) {
                        if (curr instanceof Game && s.isGame()) {
                            System.out.println("\t\t\tAdded corresponding WE Slot");
                            s.addActivity(curr);
                        } else if (curr instanceof Practice && !s.isGame()) {
                            System.out.println("\t\t\tAdded corresponding WE Slot");
                            s.addActivity(curr);
                        }
                    } else if (s.getDay() == Days.FR && s.getStartTime().equals(slot.getStartTime())) {
                        if (curr instanceof Game && s.isGame()) {
                            System.out.println("\t\t\tAdded corresponding FR Slot");
                            s.addActivity(curr);
                        } else if (curr instanceof Practice && !s.isGame()) {
                            System.out.println("\t\t\tAdded corresponding FR Slot");
                            s.addActivity(curr);
                        }
                    }
                }
            } else if (slot.getDay() == Days.TU) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.TR && s.getStartTime().equals(slot.getStartTime())) {
                        System.out.println("\t\t\tAdded corresponding TR Slot");
                        s.addActivity(curr);
                    }
                }
            }
            parent.addChild(child);
            nodeQueue.add(child);
            return true;
        }
        return false;
    }

    private static boolean noHardConstraintViolations(Slot slot, Activity curr, ANode node) {
        System.out.println("\tChecking Hard Constraint Violations of " + slot + " for " + curr + " assignment");
        if (slot.isFull()) { // 1&2) Not more than gamemax(s)/practicemax(s) activities assigned to each slot
            System.out.println("\t\tViolates full slot");
            return false; // violates constraint
        }

        // 4) not compatible activities in slot
        for (NotCompatible nc : data.getNotCompatibles()) { // check all not compatibles
            if (nc.getActivityOne() == curr) { // if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityTwo())) { // and the other activity already in slot
                    System.out.println("\t\tViolates Not Compatible 1");
                    return false; // violates constraint
                }
            } else if (nc.getActivityTwo() == curr) { // if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityOne())) { // and the other activity already in slot
                    System.out.println("\t\tViolates Not Compatible 2");
                    return false; // violates constraint
                }
            }
        }
        // 5) partial assignments of activities to slot
        for (Partial p : data.getPartials()) {
            if (p.getActivity() == curr) { // if there is a partial assignment for this activity
                if (p.getSlot().getID() != slot.getID()) { // not the correct slot for partial assignment
                    System.out.println("\t\tViolates Partial");
                    return false; // violates constraint
                }
            }
        }
        // 6) unwanted assignments of activity to slot
        for (Unwanted u : data.getUnwanteds()) {
            if (u.getActivity() == curr) { // exists an unwanted assignment containing this activity
                if (u.getSlot().getID() == slot.getID()) {
                    System.out.println("\t\tViolates Unwanted");
                    return false; // violates constraint
                }
            }
        }

        System.out.println(
                "THIS IS THE DIVISION: " + curr.getDivision() + " SLot: " + slot.getStartTime() + slot.isEveningSlot());
        if (curr.getDivision() == 9) {
            if (!slot.isEveningSlot()) {
                System.out.println("\t\tViolates evening slot");
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
                    System.out.println("\t\tfailed cause U15, U16, U17, U19 collision");
                    return false;
                }
            }
        }

        for (Slot s : node.getSlots()) {
            if (s.getStartTime() == slot.getStartTime() && s.getDay() == slot.getDay() && s != slot) {
                for (Activity a : s.getActivities()) {
                    if (a.getDivision() == curr.getDivision()) {
                        System.out.println("\t\tFailed cause same division collision ");
                        return false;
                    }
                }
            }
        }

        if (curr instanceof Game
                && slot.getDay() == Days.TU
                && slot.getStartTime() == LocalTime.of(11, 0)) {
            System.out.println("\t\tViolates \"Meeting time\"");
            return false;
        }

        for (Activity a : slot.getActivities()) {
            if (a instanceof Game && curr instanceof Practice) {
                if (curr == ((Game) a).getAssociatedPractice()) {
                    System.out.println("\t\tFailed cause associated practice");
                    return false;
                }
            } else if (a instanceof Practice && curr instanceof Game) {
                if (a == ((Game) curr).getAssociatedPractice()) {
                    System.out.println("\t\tFailed cause associated practice");
                    return false;
                }
            }
        }

        System.out.println("\t\tNo violations");
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
        for (ANode n : nodeQueue) {
            if (n.isLeaf()) { // is leaf node
                if (choose != null) { // not only leaf node
                    if (eval(n) < eval(choose)) {
                        choose = n;
                    }
                } else {
                    choose = n;
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
                    System.out.println("Chosen because partial (3.1) (3.2)");
                    return p.getActivity();
                }
            }
        }

        if (!data.getNotCompatibles().isEmpty()) { // 3.5 & 3.6
            for (NotCompatible nc : data.getNotCompatibles()) {
                if (nc.aOneAssigned) {
                    if (notAlreadyAssigned(node, nc.getActivityTwo())) {
                        System.out.println("Chosen because not compatible (3.5) (3.6)");
                        return nc.getActivityTwo();
                    }
                }
                if (nc.aTwoAssigned) {
                    if (notAlreadyAssigned(node, nc.getActivityOne())) {
                        System.out.println("Chosen because not compatible (3.5) (3.6)");
                        return nc.getActivityOne();
                    }
                }
            }
        }

        if (!data.getUnwanteds().isEmpty()) { // 3.7 & 3.8
            for (Unwanted u : data.getUnwanteds()) {
                if (notAlreadyAssigned(node, u.getActivity())) {
                    System.out.println("Chosen because unwanted (3.7) (3.8)");
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
                System.out.println("Chosen as smallest i (3.9) (3.10)");
                return ac;
            } else {
                System.out.println("not chosen because already assigned");
            }
        }

        return null;
    }

    private static boolean notAlreadyAssigned(ANode expansion_node, Activity placement_activity) {
        for (Slot s : expansion_node.getSlots()) {
            for (Activity a : s.getActivities()) {
                if (a == placement_activity) {
                    System.out.println("skipping because already placed activity in node");
                    return false;
                }
            }
        }
        return true;
    }

    private static int eval(ANode node) {
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        return (node.eval_minfilled(data.getPen_gamemin(), data.getPen_practicemin()) * data.getW_minfilled())
                + (node.eval_pref(1) * data.getW_pref())
                + (node.eval_pair(data.getPen_notpaired()) * data.getW_pair())
                + (node.eval_secdiff(data.getPen_section()) * data.getW_secdiff());
    }

    private static void printResults() {
        ANode best = null;

        for (ANode n : completedNodes) {
            if (n.getSol() == Sol.yes) {
                if (n.numberActivitiesAssigned() >= data.getActivities().size()) {
                    if (n.isLeaf()) {
                        System.out.println("n: " + eval(n) + ", best: " + eval(best));
                        if (eval(n) <= eval(best)) {
                            best = n;
                        }
                    }
                }
            }
            // if (n.numberActivitiesAssigned() >= data.getActivities().size()) {
            // if (best == null && n.getSol() == Sol.yes) {
            // best = n;
            // }
            // if (n.isLeaf()) {
            // if (best != null && eval(n) < eval(best)) {
            // best = n;
            // }
            // }
            // }=
        }
        if (best != null) {
            System.out.println(printOutput(best));
        } else {
            System.out.println("No solution found.");
        }
    }

    private static String printOutput(ANode node) {
        StringBuilder out = new StringBuilder();
        out.append("Eval-value: ").append(eval(node)).append("\n");

        TreeMap<String, String> sorted = new TreeMap<>();

        for (Slot s : node.getSlots()) {
            for (Activity a : s.getActivities()) {
                sorted.put(a.getFullIdentifier(), s.getDay().getShortCode() + ", " + s.getStartTime());
            }
        }

        sorted.forEach((key, value) -> out.append(key).append(" : ").append(value).append("\n"));

        return out.toString();
    }
}
