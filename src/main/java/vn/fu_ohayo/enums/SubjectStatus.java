package vn.fu_ohayo.enums;

public enum SubjectStatus {
    PUBLIC, DRAFT, REJECTED;
    public static SubjectStatus fromString(String status) {
        for (SubjectStatus subjectStatus : SubjectStatus.values()) {
            if (subjectStatus.name().equalsIgnoreCase(status)) {
                return subjectStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
