package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressLessonResponse;

public interface ProgressLessonService {
    ProgressLessonResponse getProgressLessons(String email, int lessonId);

    ProgressLessonResponse createProgressLesson(String username, int lessonId);

    ProgressLessonResponse updateProgressLesson(String username, int lessonId, boolean isCompleted);
}
