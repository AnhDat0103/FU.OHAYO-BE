package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressSubjectResponse;

public interface ProgressSubjectService {
    void enrollCourse(int courseId, long userId);
    ProgressSubjectResponse getProgressSubject(long userId, int subjectId);
}
