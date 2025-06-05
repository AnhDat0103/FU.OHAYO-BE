package vn.fu_ohayo.enums;

public enum ExerciseType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    FILL_IN_THE_BLANKS;
    public static ExerciseType fromString(String type) {
        for (ExerciseType exerciseType : ExerciseType.values()) {
            if (exerciseType.name().equalsIgnoreCase(type)) {
                return exerciseType;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ExerciseType.class.getCanonicalName() + "." + type);
    }
}
