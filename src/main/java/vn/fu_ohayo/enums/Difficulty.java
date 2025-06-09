package vn.fu_ohayo.enums;

public enum Difficulty {

    EASY,
    MEDIUM,
    HARD;

    public static Difficulty fromString(String difficulty) {
        for (Difficulty d : Difficulty.values()) {
            if (d.name().equalsIgnoreCase(difficulty)) {
                return d;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Difficulty.class.getCanonicalName() + "." + difficulty);
    }
}
