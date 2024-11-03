import assignments.*;
import enums.Days;
import scheduling.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        Data data = parseInput(args);


        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;

        System.out.println("\n\nExecution time in milliseconds: " + duration);
    }

    private static Data parseInput(String[] args) throws FileNotFoundException {
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
        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Days date = switch (split[0]) {
                case "MO" -> Days.MO;
                case "TU" -> Days.TU;
                case "FR" -> Days.FR;
                default -> null;
            };

            String time = split[1];
            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            data.addGameSlot(new GameSlot(date, time, max, min));
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Days date = switch (split[0]) {
                case "MO" -> Days.MO;
                case "TU" -> Days.TU;
                case "FR" -> Days.FR;
                default -> null;
            };

            String time = split[1];
            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            data.addPracticeSlot(new PracticeSlot(date, time, max, min));
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            String identifier = line;
            data.addGame(new Game(identifier));
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            String identifier = line;
            data.addPractice(new Practice(identifier));
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getGames(), data.getPractices());
            Activity a2 = findActivity(split[1], data.getGames(), data.getPractices());

            if (a1 != null && a2 != null) {
                data.addCompatible(new NotCompatible(a1, a2));
            }
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getGames(), data.getPractices());

            Slot s = findSlot(split[1], split[2], data.getGameSlots(), data.getPracticeSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addUnwanted(new Unwanted(a, s));
            }
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            String day = split[0];
            String time = split[1];
            String activityID = split[2];
            int preference = Integer.parseInt(split[3]);

            Activity a = findActivity(activityID, data.getGames(), data.getPractices());
            Slot s = findSlot(day, time, data.getGameSlots(), data.getPracticeSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addPreference(new Preference(a, s, preference));
            }
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a1 = findActivity(split[0], data.getGames(), data.getPractices());
            Activity a2 = findActivity(split[1], data.getGames(), data.getPractices());

            if (a1 != null && a2 != null) {
                data.addPair(new Pair(a1, a2));
            }
        }

        while (sc.hasNextLine() && !(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Activity a = findActivity(split[0], data.getGames(), data.getPractices());

            Slot s = findSlot(split[1], split[2], data.getGameSlots(), data.getPracticeSlots(), a instanceof Game);

            if (a != null && s != null) {
                data.addPartial(new Partial(a, s));
            }
        }
        return data;
    }

    private static Activity findActivity(String identifier, ArrayList<Game> games, ArrayList<Practice> practices) {
        for (Game g : games) {
            if (g.getTrimID().equals(identifier)) {
                return g;
            }
        }
        for (Practice p : practices) {
            if (p.getTrimID().equals(identifier)) {
                return p;
            }
        }
        return null;
    }

    private static Slot findSlot(String date, String time, ArrayList<GameSlot> gameSlots, ArrayList<PracticeSlot> pracSlots, boolean isGame) {
        if (isGame) {
            for (Slot sg : gameSlots) {
                if (sg.getDate().toString().equals(date)
                        && sg.getTime().equals(time)) {
                    return sg;
                }
            }
        } else {
            for (Slot sp : pracSlots) {
                if (sp.getDate().toString().equals(date)
                        && sp.getTime().equals(time)) {
                    return sp;
                }
            }
        }
        return null;
    }
}
