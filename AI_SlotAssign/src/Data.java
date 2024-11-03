import assignments.*;
import scheduling.Game;
import scheduling.GameSlot;
import scheduling.Practice;
import scheduling.PracticeSlot;

import java.util.ArrayList;

public class Data {
    private final String problemName;

    private int w_minfilled;
    private int w_pref;
    private int w_pair;
    private int w_secdiff;
    private int pen_gamemin;
    private int pen_practicemin;
    private int pen_notpaired;
    private int pen_section;

    private final ArrayList<GameSlot> gameSlots;
    private final ArrayList<PracticeSlot> practiceSlots;
    private final ArrayList<Game> games;
    private final ArrayList<Practice> practices;
    private final ArrayList<NotCompatible> notcompatibles;
    private final ArrayList<Unwanted> unwanteds;
    private final ArrayList<Preference> preferences;
    private final ArrayList<Pair> pairs;
    private final ArrayList<Partial> partials;

    public Data(String name, int w_minfilled, int w_pref, int w_pair, int w_secdiff,
                int pen_gamemin, int pen_practicemin, int pen_notpaired, int pen_section) {
        this.problemName = name;
        this.w_minfilled = w_minfilled;
        this.w_pref = w_pref;
        this.w_pair = w_pair;
        this.w_secdiff = w_secdiff;
        this.pen_gamemin = pen_gamemin;
        this.pen_practicemin = pen_practicemin;
        this.pen_notpaired = pen_notpaired;
        this.pen_section = pen_section;
        this.gameSlots = new ArrayList<>();
        this.practiceSlots = new ArrayList<>();
        this.games = new ArrayList<>();
        this.practices = new ArrayList<>();
        this.notcompatibles = new ArrayList<>();
        this.unwanteds = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.pairs = new ArrayList<>();
        this.partials = new ArrayList<>();
    }


    public String getProblemName() {
        return problemName;
    }

    public ArrayList<GameSlot> getGameSlots() {
        return gameSlots;
    }

    public ArrayList<PracticeSlot> getPracticeSlots() {
        return practiceSlots;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Practice> getPractices() {
        return practices;
    }

    public ArrayList<NotCompatible> getNotcompatibles() {
        return notcompatibles;
    }

    public ArrayList<Unwanted> getUnwanteds() {
        return unwanteds;
    }

    public ArrayList<Preference> getPreferences() {
        return preferences;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    public ArrayList<Partial> getPartials() {
        return partials;
    }

    public void addGameSlot(GameSlot gameSlot) {
        gameSlots.add(gameSlot);
    }

    public void addPracticeSlot(PracticeSlot practiceSlot) {
        practiceSlots.add(practiceSlot);
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void addPractice(Practice practice) {
        practices.add(practice);
    }

    public void addCompatible(NotCompatible compatible) {
        notcompatibles.add(compatible);
    }

    public void addUnwanted(Unwanted unwanted) {
        unwanteds.add(unwanted);
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
    }

    public void addPair(Pair pair) {
        pairs.add(pair);
    }

    public void addPartial(Partial partial) {
        partials.add(partial);
    }
}
