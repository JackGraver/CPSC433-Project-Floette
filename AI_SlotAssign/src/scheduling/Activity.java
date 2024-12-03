package scheduling;

import assignments.Pair;
import assignments.Preference;

/**
 * Abstract class for different possible activities (Game or Practice)
 */
public abstract class Activity {
    private final String fullIdentifier;
    private String league;
    private String ageGroup;
    private int division;
    private Preference preference;
    private Activity pair;

    public Activity(String identifier) {
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

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public Activity getPair() {
        return pair;
    }

    public void setPair(Activity pair) {
        this.pair = pair;
    }
}
