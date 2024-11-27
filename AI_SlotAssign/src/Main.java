import assignments.Assignment;
import assignments.NotCompatible;
import scheduling.ANode;
import scheduling.Activity;
import scheduling.ActivityAssignment;
import scheduling.Slot;
import utility.Data;
import utility.Setup;

import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    private static Data data;
    private static ANode root;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        System.out.println("Loading data from file.");

        data = Setup.setup(args);
//        System.out.println(data);

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;

        System.out.println("Data loading complete.\n\tExecution time in milliseconds: " + duration);

        root = new ANode(data.getActivities().size());

        System.out.println(root);

        div(root);


//        int i = 0;
//        while(i < 1) {
//            ActivityAssignment aa = f_trans();
//            if(aa.getActivity() == null && aa.getSlot() == null) {
//                break;
//            }
//            Slot s = assignments.get(assignments.indexOf(aa.getSlot()));
//            s.getActivities().add(aa.getActivity());
//
//            printAssignments();
//            i++;
//        }
    }

//    private static void printAssignments() {
//        System.out.print("\n\n(");
//        for(Slot s : assignments) {
//            System.out.print("{");
//            for(Activity a : s.getActivities()) {
//                System.out.print(a);
//            }
//            System.out.print("}");
//        }
//        System.out.print(")\n");
//    }


    private static void div(ANode node) {
        System.out.println("Currently updating node: " + node + "\n");
        Activity curr = f_trans();
        System.out.println("Assigning activity : " + curr);

        ANode child1 = node.addChild();
        child1.getSlots().get(0).add(curr);

        ANode child2 = node.addChild();
        child2.getSlots().get(1).add(curr);

        ANode child3 = node.addChild();
        child3.getSlots().get(2).add(curr);

        ANode child4 = node.addChild();
        child4.getSlots().get(3).add(curr);

        System.out.println(node);
        for(ANode c : node.getChildren()) {
            System.out.println("\t" + c);
        }
    }

    private static boolean checkAssign(Activity a) {
        return false;
    }

    private static void f_leaf() {}

    private static Activity f_trans() {
//        ActivityAssignment chosen = new ActivityAssignment();
        //1

        //2

        //3

        //3.1 & 3.2
        if(!data.getPartials().isEmpty()) {
            return data.getPartials().get(0).getActivity();
        }

        //3.3 & 3.4

        //3.5 && 3.6
//        if(data.getNotcompatibles().size() != 0) {
//            return data.getNotcompatibles().get(0).getActivityOne();
//        }
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
