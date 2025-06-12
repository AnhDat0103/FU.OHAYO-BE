package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressLessonResponse;

public interface ProgressLessonService {
    ProgressLessonResponse getProgressLessons(long userId, int lessonId);
}
