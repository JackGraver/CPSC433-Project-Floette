
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

    private static Data data;
    private static ANode root;

    public static void main(String[] args) throws Exception {
        // long startTime = System.currentTimeMillis();

        // System.out.println("Loading data from file.");

        data = Setup.setup(args);

        // long endTime = System.currentTimeMillis();
        // double duration = (endTime - startTime) / 1000.0;

        // System.out.println("Data loading complete.\n\tExecution time in milliseconds: " + duration);

        root = new ANode(data.getSlots().size());

        System.out.println(root);

        div(root);
        System.exit(0);
    }

    private static void div(ANode node) {
        boolean allSlotsFilled = true;
        for (ArrayList<Activity> slot : node.getSlots()) {
            if (slot.size() != data.getSlots().get(node.getSlots().indexOf(slot)).getMin()) {
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
    
        for (ArrayList<Activity> slot : node.getSlots()) {
            slot.add(curr);
            if (violatesHardConstraints(node, slot)) {
                slot.remove(curr); 
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
                div(child); 
                break; 
            }
        }
    }
    
    private static boolean gameMaxCheck(ANode node, ArrayList<Activity> slot) {
            int gameCount = 0;
            for (Activity activity : slot) {
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

    private static boolean practiceMaxCheck(ANode node, ArrayList<Activity> slot) {
            int practiceCount = 0;
            for (Activity activity : slot) {
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

    private static boolean checkPracGameConflict(ANode node, ArrayList<Activity> slot) {
            HashSet<String> pracDiv = new HashSet<>();
            HashSet<String> gameDiv = new HashSet<>();
        
            for (Activity activity : slot) {
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

    private static boolean violatesNotCompatible(ANode node, ArrayList<Activity> slot) {
        for (NotCompatible nc : data.getNotcompatibles()) {
                if (slot.contains(nc.getActivityOne()) && slot.contains(nc.getActivityTwo())) {
                    System.out.println("Violated Not Compatible");
                    return true;
                }
            }
        return false;
    }

    private static boolean violatesPartAssign(ANode node, ArrayList<Activity> slot) {
        for (Partial partial : data.getPartials()) {
            Activity activity = partial.getActivity();
            boolean isAssignedCorrectly = false;
            if (slot.contains(activity)) {
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

    
    private static boolean violatesUnwanted(ANode node, ArrayList<Activity> slot) {
        for (Unwanted unwanted : data.getUnwanteds()){
            Activity activity = unwanted.getActivity();
            Slot unwantedSlot = unwanted.getSlot();
                if (slot.contains(activity) && data.getSlots().get(node.getSlots().indexOf(slot)).equals(unwantedSlot)){
                    System.out.println("Violated Unwanted");
                    return true;
                }
        }
        return false;
    }

    private static boolean violatesHardConstraints(ANode node, ArrayList<Activity> slot) {
        return gameMaxCheck(node, slot) || practiceMaxCheck(node, slot) || checkPracGameConflict(node, slot) || violatesNotCompatible(node, slot)  || violatesPartAssign(node, slot) || violatesUnwanted(node, slot);
    }

    private static Activity f_trans() {
        if(!data.getPartials().isEmpty()) {
            Activity activity = data.getPartials().get(0).getActivity();
            return activity;
        }

        if (!data.getActivities().isEmpty()) {
            Activity activity = data.getActivities().get(0);
            return activity;
        }
        return null;
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
