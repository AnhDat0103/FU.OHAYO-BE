package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {

    LessonResponse createLesson(LessonRequest lessonRequest);

    List<LessonResponse> getAllLessons();

    LessonResponse updateLesson(Integer id , LessonPatchRequest lessonRequest);

    void deleteLesson(Integer id);

    Page<LessonResponse> getAllLessons(int subjectId, int page, int size);

    LessonResponse getLessonById(int id);
}
