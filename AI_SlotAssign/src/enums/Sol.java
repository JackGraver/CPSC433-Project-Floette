package enums;

public enum Sol {
    none("?"),
    yes("yes");

    private final String sol;

    Sol(String sol) { this.sol = sol; }

    public String getSol() {
        return sol;
    }
}
