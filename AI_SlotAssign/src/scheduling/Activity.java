package scheduling;

import assignments.Pair;
import assignments.Preference;

/**
 * Abstract class for different possible activities (Game or Practice)
 */
public abstract class Activity {
    private String identifier;
    private String trimId;
    private String league;
    private String ageGroup;
    private int division;
    private Preference preference;
    private Pair pair;

    public Activity(String identifier) {
        this.identifier = identifier;
        this.trimId = identifier.replace(" ", "");
        parse(identifier);
    }

    private void parse(String identifier) {
        identifier = identifier.replace("  ", " ");
        String[] split = identifier.split(" ");
        this.league = split[0].trim();
        this.ageGroup = split[1].trim();
        this.division = Integer.parseInt(split[3].trim());
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getTrimID() {
        return trimId;
    }

    public String toString() {
        return identifier;
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

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }
}
