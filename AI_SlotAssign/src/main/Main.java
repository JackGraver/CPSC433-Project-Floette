package main;

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

import java.util.ArrayList;

import assignments.NotCompatible;
import assignments.Partial;
import assignments.Unwanted;

public class Main {

    public static Data data;
    private static ANode root;
    private static ArrayList<ANode> nodeQueue;

    public static void main(String[] args) throws Exception {
        data = Setup.setup(args); //load data from given file

        nodeQueue = new ArrayList<>(); //create queue for going through expansion of leaves

        root = new ANode(data.getSlots()); //initialize root node
        nodeQueue.add(root); //add to queue (to be expanded first)

        //is a for loop for testing
        while (true) { //while there are nodes to expand (not sol = yes)
            ANode expansion_node = f_leaf(); //get next node to expand (f_leaf)
            Activity placement_activity = f_trans(); //get next activity to place in expanded node (f_trans)
            if (placement_activity == null) {
                System.out.println("No more activities to assign. Soling " + expansion_node + " to yes");
                for (ANode n : nodeQueue) {
                    if (n.isLeaf()) {
                        n.setSol(Sol.yes);
                    }
                }
                break;
            }
            div(expansion_node, placement_activity); //handle expansion
            data.getActivities().remove(placement_activity);
            data.removeAllActivity(placement_activity);

        }
        printTree(root);
        ANode best = null;
        for (ANode n : nodeQueue) {
            if (n.isLeaf()) {
                if (best == null) {
                    best = n;
                } else {
                    if (eval(n) < eval(best)) {
                        best = n;
                    }
                }
            }

        }
        System.out.println("Best option is: " + best);

    }

    private static void printTree(ANode n) {
//        System.out.println("\nCurrent Tree:");
//        System.out.println(root);
//        System.out.println();
        System.out.println(n);
    }

    /**
     * Handles expansion of a node given an activity to assign
     *
     * @param node - node being expanded
     * @param curr - activity being assigned to slots
     */
    private static void div(ANode node, Activity curr) {
        ArrayList<Integer> assigned = new ArrayList<>(); //make sure we don't choose the same slot for each expansion leaf (could use better solution)

        //loop through number of slots (n) to potentially create n children nodes
        for (int i = 0; i < node.getSlots().size(); i++) {
            ANode child = new ANode(node.getSlots());
            for (Slot slot : child.getSlots()) {
                if (!assigned.contains(child.getSlots().indexOf(slot))) {
                    //check if it's the correct type of slot (Game Slot for Games, Prac slot for Practices)
                    if (slot.getType() == SlotType.Game && curr instanceof Game) { //Game
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            assigned.add(child.getSlots().indexOf(slot));
                            break;
                        }
                    } else if (slot.getType() == SlotType.Practice && curr instanceof Practice) { //Prac
                        if (assignActivityToSlot(node, child, slot, curr)) {
                            assigned.add(child.getSlots().indexOf(slot));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs the assignment of an activity to a given slot for a new child node of the current node being expanded
     * <br>-Also does hard constraint of adding activity on Monday to equivalent slot on Wednesday and Friday, similarly with Tuesday to equivalent Thursday slot
     *
     * @param parent - parent being expanded
     * @param child  - new child node of parent
     * @param slot   - slot of child activity is being added to
     * @param curr   - activity being added
     * @return if assignment was successful
     * \
     */
    private static boolean assignActivityToSlot(ANode parent, ANode child, Slot slot, Activity curr) {
        if (noHardConstraintViolations(slot, curr)) {
            slot.addActivity(curr);
            if (slot.getDay() == Days.MO) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.WE && s.getTime().equals(slot.getTime())) {
                        s.addActivity(curr);
                    } else if (s.getDay() == Days.FR && s.getTime().equals(slot.getTime())) {
                        s.addActivity(curr);
                    }
                }
            } else if (slot.getDay() == Days.TU) {
                for (Slot s : child.getSlots()) {
                    if (s.getDay() == Days.TR && s.getTime().equals(slot.getTime())) {
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

    private static boolean noHardConstraintViolations(Slot slot, Activity curr) {
        if (slot.isFull()) { //1&2) Not more than gamemax(s) activities assigned to each slot
            return false;
        }
        //4) not compatible activities in slot
        for (NotCompatible nc : data.getNotCompatibles()) { //check all not compatibles
            if (nc.getActivityOne() == curr) { //if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityTwo())) { //and the other activity already in slot
                    return false; //violates constraint, go try next slot
                }
            } else if (nc.getActivityTwo() == curr) { //if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityOne())) { //and the other activity already in slot
                    return false; //violates constraint, go try next slot
                }
            }
        }
        //5) partial assignments of activities to slot
        for (Partial p : data.getPartials()) {
            if (p.getActivity() == curr) { //if there is a partassign for this activity
                if (p.getSlot().getID() != slot.getID()) {
                    return false; //exit the loop because the activity has been assigned for this node
                }
            }
        }
        //6) unwanted assignments of activity to slot
        for (Unwanted u : data.getUnwanteds()) {
            if (u.getActivity() == curr) { //exists an unwanted assignment containing this activity
                if (u.getSlot().getID() == slot.getID()) {
                    return false; //go to next slot as this activity cannot be assigned to this slot
                }
            }
        }
        return true;
    }

    /**
     * Defines what leaves will be chosen to be explored (expanded by div)
     * <br>- Chooses leaf at the largest depth
     * <br>- In case of ties for largest depth, we choose the node such that Eval(assign) is minimized
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
     * - If no hard constraint violations, chooses the first activity yet to be assigned
     *
     * @return Activity - Game or Practice to be assigned to slot(s)
     */
    public static Activity f_trans() {
        if (data.getActivities().isEmpty()) { //1
            return null;
        }

        //2?

        if (!data.getPartials().isEmpty()) { //3.1 & 3.2
            return data.getPartials().get(0).getActivity();
        }

        //3.3 & 3.4

        if (!data.getNotCompatibles().isEmpty()) { //3.5 & 3.6
            if (data.getNotCompatibles().get(0).aTwoAssigned) {
                data.getNotCompatibles().get(0).aTwoAssigned = true;
                return data.getNotCompatibles().get(0).getActivityOne();
            }
            if (data.getNotCompatibles().get(0).aOneAssigned) {
                data.getNotCompatibles().get(0).aOneAssigned = true;
                return data.getNotCompatibles().get(0).getActivityTwo();
            }
//            return data.getNotCompatibles().get(0).getActivityOne();
        }

        if (!data.getUnwanteds().isEmpty()) { //3.7 & 3.8
            return data.getUnwanteds().get(0).getActivity();
        }

        return data.getActivities().get(0);
    }

    private static int eval(ANode node) {
        return (node.eval_minfilled() * data.getW_minfilled())
                + (node.eval_pref() * data.getW_pref())
                + (node.eval_pair() * data.getW_pair())
                + (node.eval_secdiff() * data.getW_secdiff());
    }
}
