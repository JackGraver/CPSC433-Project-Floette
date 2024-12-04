package utility;

import assignments.*;
import enums.Days;
import scheduling.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Setup {

    private static String[] mwfGamePracTimes = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00", "19:00", "20:00"};
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
        // Game Slots
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            GameSlot game = (GameSlot) createActivity(line, true);

            // if (checkValidGameTime(time, date)) {
            data.addSlot(game);
            // }
        }

        // Practice Slots
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            PracticeSlot prac = (PracticeSlot) createActivity(line, false);

            // if (checkValidPracticeTime(time)) {
            data.addSlot(prac);
            // }
        }

        // Games
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            Game game = new Game(line);
            data.addActivity(game);
            if (game.getDivision() == 9) {
                data.getEveningActivities().add(new Evening(game));
            }
        }

        // Practices
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            Practice prac = new Practice(line);
            Game g;
            if ((g = findAssociatedGame(prac, data.getActivities())) != null) {
                g.setAssociatedPractice(prac);
            }
            data.addActivity(new Practice(line));
            if (prac.getDivision() == 9) {
                data.getEveningActivities().add(new Evening(prac));
            }
        }

        // Not Compatible
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            line = line.replaceAll("\\s{2,}", " ").trim();
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getActivities());
            Activity a2 = findActivity(split[1], data.getActivities());

            if (a1 != null && a2 != null) {
                data.addCompatible(new NotCompatible(a1, a2));
            }
        }

        // Unwanted
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            line = line.replaceAll("\\s{2,}", " ").trim();

            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getActivities());

            Slot s = findSlot(split[1], split[2], data.getSlots());
            if (a != null && s != null) {
                data.addUnwanted(new Unwanted(a, s));
            }
        }

        // Preferences
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            line = line.replaceAll("\\s{2,}", " ").trim();
            String[] split = line.split(",");

            String day = split[0];
            String time = split[1];
            String activityID = split[2];
            int preference = Integer.parseInt(split[3].trim());

            Activity a = findActivity(activityID, data.getActivities());
            Slot s = findSlot(day, time, data.getSlots());

            if (a != null && s != null) {
                a.setPreference(new Preference(s, preference));
            }
        }

        // Pairs
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            line = line.replaceAll("\\s{2,}", " ").trim();
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getActivities());
            Activity a2 = findActivity(split[1], data.getActivities());

            if (a1 != null && a2 != null) {
                a1.setPair(a2);
                a2.setPair(a1);
                data.addPair(new Pair(a1, a2));
            }
        }

        // Partials
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":") && !line.equals("")) {
            line = line.replaceAll("\\s{2,}", " ").trim();
            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getActivities());

            Slot s = findSlot(split[1], split[2], data.getSlots());

            if (a != null && s != null) {
                data.addPartial(new Partial(a, s));
            }
        }
        return data;
    }

    private static Slot createActivity(String line, boolean isGame) {
        line = line.replaceAll("\\s{2,}", " ").trim();
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
        int max = Integer.parseInt(split[2].trim());
        int min = Integer.parseInt(split[3].trim());
        if (isGame) {
            return new GameSlot(date, time, max, min);
        } else {
            return new PracticeSlot(date, time, max, min);
        }
    }

    private static Activity findActivity(String identifier, ArrayList<Activity> activities) {
        identifier = identifier.trim();
        for (Activity a : activities) {
            if (a.getFullIdentifier().equals(identifier)) {
                return a;
            }
        }
        return null;
    }

    private static Slot findSlot(String date, String time, ArrayList<Slot> slots) {
        String[] split = time.replace(" ", "").split(":");
        LocalTime checkTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        for (Slot s : slots) {
            if (s.getDay().getShortCode().trim().equalsIgnoreCase(date.trim())
                    && s.getStartTime().equals(checkTime)) {
                return s;
            }
        }
        return null;
    }

    private static Game findAssociatedGame(Practice prac, ArrayList<Activity> activities) {
        for (Activity a : activities) {
            if (a instanceof Game) {
                if (a.getDivision() == prac.getDivision()
                        && a.getAgeGroup().equals(prac.getAgeGroup())
                        && a.getDivision() == prac.getDivision()) {
                    return (Game) a;
                }
            }
        }
        return null;
    }

    private static boolean checkValidGameTime(String time, Days day) {
        if (day == Days.MO || day == Days.WE || day == Days.FR) {
            return Arrays.asList(mwfGamePracTimes).contains(time);
        } else if (day == Days.TU || day == Days.TR) {
            return Arrays.asList(ttrGamesTimes).contains(time);
        }
        return false;
    }

    private static boolean checkValidPracticeTime(String time) {
        return Arrays.asList(mwfGamePracTimes).contains(time);
    }
}
