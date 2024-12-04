package scheduling;

import assignments.Pair;
import assignments.Preference;

import java.util.ArrayList;

/**
 * Abstract class for different possible activities (Game or Practice)
 */
public abstract class Activity {
    private final String fullIdentifier;
    private String league;
    private String ageGroup;
    private int division;
    private ArrayList<Preference> preferences;
    private ArrayList<Activity> pairs;

    public Activity(String identifier) {
        preferences = new ArrayList<>();
        pairs = new ArrayList<>();
        this.fullIdentifier = identifier.replaceAll("\\s{2,}", " ").trim();
        parseIdentifierString(fullIdentifier);
    }

    private void parseIdentifierString(String identifier) {
        String[] split = identifier.split(" ");
        this.league = split[0].trim();
        this.ageGroup = split[1].trim();
        this.division = Integer.parseInt(split[3].trim());
    }

    public String getFullIdentifier() {
        return fullIdentifier;
    }

    public String toString() {
        return fullIdentifier;
    }

    public String getLeague() {
        return league;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public int getDivision() {
        return division;
    }

    public String getLeagueAgeGroupDivision() {
        return league + " " + ageGroup + " " + division;
    }

    public ArrayList<Preference> getPreference() {
        return preferences;
    }

    public void setPreference(Preference preference) {
        this.preferences.add(preference);
    }

    public ArrayList<Activity> getPairs() {
        return pairs;
    }

    public void setPair(Activity pair) {
        this.pairs.add(pair);
    }
}
