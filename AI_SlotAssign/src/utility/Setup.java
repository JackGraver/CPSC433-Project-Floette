package utility;

import assignments.*;
import enums.Days;
import scheduling.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Setup {

    private static String[] mwfGamePracTimes = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
    private static String[] fPracTimes = {"8:00", "10:00", "12:00", "14:00", "16:00", "18:00"};
    private static String[] ttrGamesTimes = {"8:00", "9:30", "11:00", "12:30", "14:00", "15:30", "17:00", "18:30"};

    public static Data setup(String[] args) throws FileNotFoundException {
        if (args.length < 8) {
            System.out.println("Invalid command line arguments.");
            System.exit(0);
        }
        int w_minfilled = Integer.parseInt(args[1]);
        int w_pref = Integer.parseInt(args[2]);
        int w_pair = Integer.parseInt(args[3]);
        int w_secdiff = Integer.parseInt(args[4]);
        int pen_gamemin = Integer.parseInt(args[5]);
        int pen_practicemin = Integer.parseInt(args[6]);
        int pen_notpaired = Integer.parseInt(args[7]);
        int pen_section = Integer.parseInt(args[8]);

        Scanner sc = new Scanner(new File(args[0]));
        sc.nextLine();
        String name = sc.nextLine();
        Data data = new Data(name, w_minfilled, w_pref, w_pair, w_secdiff,
                pen_gamemin, pen_practicemin, pen_notpaired, pen_section);

        sc.nextLine();
        String line;
        //Game Slots
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Days date = switch (split[0]) {
                case "MO" -> Days.MO;
                case "TU" -> Days.TU;
                case "WE" -> Days.WE;
                case "TR" -> Days.TR;
                case "FR" -> Days.FR;
                default -> null;
            };

            String time = split[1];
            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            if (checkValidGameTime(time, date)) {
                data.addSlot(new GameSlot(date, time, max, min));
            }
        }

        //Practice Slots
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Days date = switch (split[0]) {
                case "MO" -> Days.MO;
                case "TU" -> Days.TU;
                case "WE" -> Days.WE;
                case "TR" -> Days.TR;
                case "FR" -> Days.FR;
                default -> null;
            };

            String time = split[1];

            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            if (checkValidPracticeTime(time)) {
                data.addSlot(new PracticeSlot(date, time, max, min));
            }
        }

        //Games
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            data.addActivity(new Game(line.replace("  ", " ")));
        }

        //Practices
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            data.addActivity(new Practice(line.replace("  ", " ")));
        }

        //Not Compatible
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getActivities());
            Activity a2 = findActivity(split[1], data.getActivities());

            if (a1 != null && a2 != null) {
                data.addCompatible(new NotCompatible(a1, a2));
            }
        }

        //Unwanted
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getActivities());

            Slot s = findSlot(split[1], split[2], data.getSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addUnwanted(new Unwanted(a, s));
            }
        }

        //Preferences
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            String day = split[0];
            String time = split[1];
            String activityID = split[2];
            int preference = Integer.parseInt(split[3]);

            Activity a = findActivity(activityID, data.getActivities());
            Slot s = findSlot(day, time, data.getSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addPreference(new Preference(a, s, preference));
            }
        }

        //Pairs
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getActivities());
            Activity a2 = findActivity(split[1], data.getActivities());

            if (a1 != null && a2 != null) {
                data.addPair(new Pair(a1, a2));
            }
        }

        //Partials
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getActivities());

            Slot s = findSlot(split[1], split[2], data.getSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addPartial(new Partial(a, s));
            }
        }
        return data;
    }

    private static Activity findActivity(String identifier, ArrayList<Activity> activities) {
        for (Activity a : activities) {
            if (a.getTrimID().equals(identifier)) {
                return a;
            }
        }
//        for (Practice p : practices) {
//            if (p.getTrimID().equals(identifier)) {
//                return p;
//            }
//        }
        return null;
    }

    private static Slot findSlot(String date, String time, ArrayList<Slot> slots, boolean isGame) {
//        if (isGame) {
//            for (Slot sg : gameSlots) {
//                if (sg.getDay().getShortCode().equals(date)
//                        && sg.getTime().equals(time)) {
//                    return sg;
//                }
//            }
//        } else {
//            for (Slot sp : pracSlots) {
//                if (sp.getDay().getShortCode().equals(date)
//                        && sp.getTime().equals(time)) {
//                    return sp;
//                }
//            }
//        }
        for (Slot s : slots) {
            if (s.getDay().getShortCode().equals(date)
                    && s.getTime().equals(time)) {
                return s;
            }
        }
        return null;
    }

    private static boolean checkValidGameTime(String time, Days day) {
        if (day == Days.MO || day == Days.WE || day == Days.FR) {
            if (Arrays.asList(mwfGamePracTimes).contains(time)) {
                return true;
            }
        } else if (day == Days.TU || day == Days.TR) {
            if (Arrays.asList(ttrGamesTimes).contains(time)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkValidPracticeTime(String time) {
        if (Arrays.asList(mwfGamePracTimes).contains(time)) {
            return true;
        }
        return false;
    }
}
