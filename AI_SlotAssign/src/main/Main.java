package main;

import enums.SlotType;
import scheduling.ANode;
import scheduling.Activity;
import scheduling.Game;
import scheduling.Practice;
import scheduling.Slot;
import utility.Data;
import utility.Setup;

import java.util.ArrayList;
import java.util.HashSet;

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
        for (int i = 0; i < 4; i++) { //while there are nodes to expand (not sol = yes)
            ANode expansion_node = f_leaf(); //get next node to expand (f_leaf)
            Activity placement_activity = f_trans(); //get next activity to place in expanded node (f_trans)
            if (placement_activity == null) {
                System.out.println("No more activities to assign. Ending.");
                break;
            }
            div(expansion_node, placement_activity); //handle expansion
            data.getActivities().remove(placement_activity);
            data.removeAllActivity(placement_activity);

        }
        printTree(root);
    }

    private static void printTree(ANode n) {
//        System.out.println("\nCurrent Tree:");
//        System.out.println(root);
//        System.out.println();
        System.out.println(n);
    }

    private static void div(ANode node, Activity curr) {
//        boolean allSlotsFilled = true;
//        for (Slot slot : node.getSlots()) {
//            if (slot.getActivities().size() != data.getSlots().get(node.getSlots().indexOf(slot)).getMin()) {
//                allSlotsFilled = false;
//                break;
//            }
//        }
//
//        if (allSlotsFilled || data.getActivities().isEmpty()) {
//            System.out.println("All slots are filled or no activities left. Ending.");
//            System.out.println("Finished Tree:" + "\n" + node);
//            return;
//        }

        ArrayList<Integer> assigned = new ArrayList<>(); //make sure we don't choose the same slot for each expansion leaf (could use better solution)

        //loop through number of slots (n) to potentially create n children nodes
        for (int i = 0; i < node.getSlots().size(); i++) {
            ANode child = new ANode(node.getSlots());
            for (Slot slot : child.getSlots()) {
                if (!assigned.contains(child.getSlots().indexOf(slot))) {
                    //check if it's the correct type of slot (Game Slot for Games, Prac slot for Practices)
                    if (slot.getType() == SlotType.Game && curr instanceof Game) { //Game
                        if (!violates(slot, curr)) {
                            slot.addActivity(curr);
                            node.addChild(child);
                            nodeQueue.add(child);
                            assigned.add(child.getSlots().indexOf(slot));
                            break;
                        }
                    } else if (slot.getType() == SlotType.Practice && curr instanceof Practice) { //Prac
                        if (!violates(slot, curr)) {
                            slot.addActivity(curr);
                            node.addChild(child);
                            nodeQueue.add(child);
                            assigned.add(child.getSlots().indexOf(slot));
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean violates(Slot slot, Activity curr) {
        if (slot.isFull()) { //1&2) Not more than gamemax(s) activities assigned to each slot
            return true;
        }
        //4) not compatible activities in slot
        for (NotCompatible nc : data.getNotCompatibles()) { //check all not compatibles
            if (nc.getActivityOne() == curr) { //if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityTwo())) { //and the other activity already in slot
                    return true; //violates constraint, go try next slot
                }
            } else if (nc.getActivityTwo() == curr) { //if activity to be added is one of the not compatible activities
                if (slot.getActivities().contains(nc.getActivityOne())) { //and the other activity already in slot
                    return true; //violates constraint, go try next slot
                }
            }
        }
        //5) partial assignments of activities to slot
        for (Partial p : data.getPartials()) {
            if (p.getActivity() == curr) { //if there is a partassign for this activity
                if (p.getSlot().getID() != slot.getID()) {
                    return true; //exit the loop because the activity has been assigned for this node
                }
            }
        }
        //6) unwanted assignments of activity to slot
        for (Unwanted u : data.getUnwanteds()) {
            if (u.getActivity() == curr) { //exists an unwanted assignment containing this activity
                if (u.getSlot().getID() == slot.getID()) {
                    return true; //go to next slot as this activity cannot be assigned to this slot
                }
            }
        }
        return false;
    }

    /**
     * Defines what leaves will be chosen to be explored (expanded by div) first.
     * Chooses leaf at the largest depth
     * In case of multiple
     *
     * @return
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

    public static Activity f_trans() {
        if (data.getActivities().isEmpty()) {

        }

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

    private static boolean gameMaxCheck(ANode node, Slot slot) {
        int gameCount = 0;
        for (Activity activity : slot.getActivities()) {
            if (activity instanceof Game) {
                gameCount++;
                if (gameCount > 1) {
                    System.out.println("Violated Game Count");
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean practiceMaxCheck(ANode node, Slot slot) {
        int practiceCount = 0;
        for (Activity activity : slot.getActivities()) {
            if (activity instanceof Practice) {
                practiceCount++;
                if (practiceCount > 1) {
                    System.out.println("Violated Practice Count");
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkPracGameConflict(ANode node, Slot slot) {
        HashSet<String> pracDiv = new HashSet<>();
        HashSet<String> gameDiv = new HashSet<>();

        for (Activity activity : slot.getActivities()) {
            String[] parts = activity.getIdentifier().split(" ");
            String leagueAgeGroupDivision = parts[0] + " " + parts[1] + " " + parts[2];
            if (activity instanceof Practice) {
                pracDiv.add(leagueAgeGroupDivision);
            } else if (activity instanceof Game) {
                gameDiv.add(leagueAgeGroupDivision);
            }
        }
        for (String div : pracDiv) {
            if (gameDiv.contains(div)) {
                System.out.println("Violated Practice Game Conflict");
                return true;
            }
        }
        return false;
    }

    private static boolean violatesNotCompatible(ANode node, Slot slot) {
        for (NotCompatible nc : data.getNotCompatibles()) {
            if (slot.getActivities().contains(nc.getActivityOne()) && slot.getActivities().contains(nc.getActivityTwo())) {
                System.out.println("Violated Not Compatible");
                return true;
            }
        }
        return false;
    }

    private static boolean violatesPartAssign(ANode node, Slot slot) {
        for (Partial partial : data.getPartials()) {
            Activity activity = partial.getActivity();
            boolean isAssignedCorrectly = false;
            if (slot.getActivities().contains(activity)) {
                isAssignedCorrectly = true;
                break;
            }
            if (!isAssignedCorrectly) {
                System.out.println("Violated Partial Assign for activity: " + activity);
                return true;
            }
        }
        return false;
    }


    private static boolean violatesUnwanted(ANode node, Slot slot) {
        for (Unwanted unwanted : data.getUnwanteds()) {
            Activity activity = unwanted.getActivity();
            Slot unwantedSlot = unwanted.getSlot();
            if (slot.getActivities().contains(activity) && data.getSlots().get(node.getSlots().indexOf(slot)).equals(unwantedSlot)) {
                System.out.println("Violated Unwanted");
                return true;
            }
        }
        return false;
    }

    private static boolean violatesHardConstraints(ANode node, Slot slot) {
        return gameMaxCheck(node, slot)
                || practiceMaxCheck(node, slot)
                || checkPracGameConflict(node, slot)
                || violatesNotCompatible(node, slot)
                || violatesPartAssign(node, slot)
                || violatesUnwanted(node, slot);
    }

    private static int eval(ANode node) {
        return (node.eval_minfilled() * data.getW_minfilled())
                + (node.eval_pref() * data.getW_pref())
                + (node.eval_pair() * data.getW_pair())
                + (node.eval_secdiff() * data.getW_secdiff());
    }
}
