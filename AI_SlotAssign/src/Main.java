import assignments.*;
import enums.Date;
import enums.SlotType;
import scheduling.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = args[0];

        Scanner sc = new Scanner(new File(fileName));
        sc.nextLine();
        String name = sc.nextLine();

        ArrayList<GameSlot> gameSlots = new ArrayList<>();
        sc.nextLine();
        String line;
        while (!(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Date date = switch (split[0]) {
                case "MO" -> Date.MO;
                case "TU" -> Date.TU;
                case "FR" -> Date.FR;
                default -> null;
            };

            String time = split[1];
            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            gameSlots.add(new GameSlot(date, time, max, min));
        }

        ArrayList<PracticeSlot> pracSlots = new ArrayList<>();
        while (!(line = sc.nextLine().trim()).endsWith(":")) {
            line = line.replace(" ", "");
            String[] split = line.split(",");

            Date date = switch (split[0]) {
                case "MO" -> Date.MO;
                case "TU" -> Date.TU;
                case "FR" -> Date.FR;
                default -> null;
            };

            String time = split[1];
            int max = Integer.parseInt(split[2]);
            int min = Integer.parseInt(split[3]);

            pracSlots.add(new PracticeSlot(date, time, max, min));
        }

        System.out.println(gameSlots);
        System.out.println(pracSlots);

        ArrayList<Game> games = new ArrayList<>();
        while (!(line = sc.nextLine().trim()).endsWith(":")) {
            String identifier = line;
            games.add(new Game(identifier));
        }

        ArrayList<Practice> practices = new ArrayList<>();
        while (!(line = sc.nextLine().trim()).endsWith(":")) {
            String identifier = line;
            practices.add(new Practice(identifier));
        }

        System.out.println(games);
        System.out.println(practices);

//        ArrayList<NotCompatible> notCompatibles = new ArrayList<>();
//        ArrayList<Unwanted> unwanteds = new ArrayList<>();
//        ArrayList<Preference> preferences = new ArrayList<>();
//        ArrayList<Pair> pairs = new ArrayList<>();
//        ArrayList<Partial> partials = new ArrayList<>();


    }
}
