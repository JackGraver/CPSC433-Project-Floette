package main;

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

        printTree();

        while(!nodeQueue.isEmpty()) { //while there are nodes to expand (not sol = yes)
            div(nodeQueue.remove(0));
        }
    }

    private static void printTree() {
        System.out.println(root);
    }

    private static void div(ANode node) {
        boolean allSlotsFilled = true;
        for (Slot slot : node.getSlots()) {
            if (slot.getActivities().size() != data.getSlots().get(node.getSlots().indexOf(slot)).getMin()) {
                allSlotsFilled = false;
                break; 
            }
        }
    
        if (allSlotsFilled || data.getActivities().isEmpty()) {
            System.out.println("All slots are filled or no activities left. Ending.");
            System.out.println("Finished Tree:" + "\n" + node);
            return; 
        }
    
        System.out.println("Currently updating node: " + node + "\n");
        Activity curr = f_trans();
        if (curr == null) {
            System.out.println("No more activities to assign. Ending.");
            return;
        }
    
        System.out.println("Assigning activity: " + curr);
    
        for (Slot slot : node.getSlots()) {
            slot.getActivities().add(curr);
            if (violatesHardConstraints(node, slot)) {
                slot.getActivities().remove(curr);
                continue; 

            } else {
                if (!data.getPartials().isEmpty()) {
                    data.getPartials().remove(0);
                } else if (!data.getActivities().isEmpty()) {
                    data.getActivities().remove(0);
                }

                System.out.println("Activity assigned to slot: " + slot + "\n");
                ANode child = node.addChild();
                child.setSlots(node.getSlots());
                nodeQueue.add(child);
                break; 
            }
        }
    }

    private static Activity f_leaf() {
        return null;
    }

    public static Activity f_trans() {
        if(data.getActivities().isEmpty()) {

        }

        if(!data.getPartials().isEmpty()) { //3.1 & 3.2
            Activity a = data.getPartials().remove(0).getActivity();
            data.getActivities().remove(a);
            return a;
        }

        if(!data.getNotcompatibles().isEmpty()) { //3.3 & 3.4
            Activity a = data.getNotcompatibles().remove(0).getActivityOne();
            data.getActivities().remove(a);
            return a;
        }

        //3.5 & 3.6

        if(!data.getUnwanteds().isEmpty()) { //3.7 & 3.8
            Activity a = data.getUnwanteds().remove(0).getActivity();
            data.getActivities().remove(a);
            return a;
        }

        return data.getActivities().remove(0);
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
        for (NotCompatible nc : data.getNotcompatibles()) {
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
        for (Unwanted unwanted : data.getUnwanteds()){
            Activity activity = unwanted.getActivity();
            Slot unwantedSlot = unwanted.getSlot();
                if (slot.getActivities().contains(activity) && data.getSlots().get(node.getSlots().indexOf(slot)).equals(unwantedSlot)){
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

    private int eval() {
        return (eval_minfilled() * data.getW_minfilled())
                + (eval_pref() * data.getW_pref())
                + (eval_pair() * data.getW_pair())
                + (eval_secdiff() * data.getW_secdiff());
    }

    private int eval_minfilled() {
        return 0;
    }

    private int eval_pref() {
        return 0;
    }

    private int eval_pair() {
        return 0;
    }

    private int eval_secdiff() {
        return 0;
    }
}
