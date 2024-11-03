package enums;

public enum Days {
    MO("Monday"),
    TU("Tuesday"),
    FR("Friday");

    private final String dayName;

    Days(String dayName) {
        this.dayName = dayName;
    }

    @Override
    public String toString() {
        return dayName;
    }

    public String getShortCode() {
        return name();
    }
}
