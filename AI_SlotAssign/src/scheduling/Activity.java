package scheduling;

public abstract class Activity {
    private String identifier;

    public Activity(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return identifier;
    }
}
