package scheduling;

import java.util.Arrays;

/**
 * Abstract class for different possible activities (Game or Practice)
 */
public abstract class Activity {
    private String identifier;
    private String trimId;
    private String league;
    private String ageGroup;
    private int division;

    public Activity(String identifier) {
        this.identifier = identifier;
        this.trimId = identifier.replace(" ", "");
//        parse(identifier);
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

}
