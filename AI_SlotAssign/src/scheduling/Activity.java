package scheduling;

import java.util.Arrays;

public abstract class Activity {
    private String identifier;
    private String trimId;
    private String league;
    private String ageGroup;
    private int division;

    public Activity(String identifier) {
        this.identifier = identifier;
        this.trimId = identifier.replace(" ", "");
        System.out.println(identifier + " vs " + trimId);
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

//    public int compareTo(Activity other) {
//        int c;
//
//        if ((c = this.league.compareTo(other.league)) != 0) {
//            return c; // Return comparison result of league
//        }
//
//        if ((c = this.ageGroup.compareTo(other.ageGroup)) != 0) {
//            return c; // Return comparison result of ageGroup
//        }
//
//        // Use Integer.compare for division comparison
//        return Integer.compare(this.division, other.division);
//    }
}
