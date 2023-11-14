package ro.fasttrackit.homework;

public enum TimeClassification {
    RED,
    YELLOW,
    GREEN;

    public static TimeClassification fromDuration(long hours) {
        if (hours < 10) {
            return RED;
        } else if (hours < 30) {
            return YELLOW;
        } else {
            return GREEN;
        }
    }
}
