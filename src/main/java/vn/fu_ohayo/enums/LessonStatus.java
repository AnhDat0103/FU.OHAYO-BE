package vn.fu_ohayo.enums;

public enum LessonStatus {

    PUBLIC,
    PRIVATE,
    DRAFT;

    public static LessonStatus fromString(String status) {
        for (LessonStatus lessonStatus : LessonStatus.values()) {
            if (lessonStatus.name().equalsIgnoreCase(status)) {
                return lessonStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + status + " found");
    }
}
