package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressLessonResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.ProgressLesson;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressLessonMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.ProgressLessonRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressLessonService;

@Service
public class ProgressLessonServiceImp  implements ProgressLessonService {

    private final ProgressLessonRepository progressLessonRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final ProgressLessonMapper progressLessonMapper;

    public ProgressLessonServiceImp(ProgressLessonRepository progressLessonRepository,
                                    UserRepository userRepository,
                                    LessonRepository lessonRepository,
                                    ProgressLessonMapper progressLessonMapper) {
        this.progressLessonRepository = progressLessonRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.progressLessonMapper = progressLessonMapper;
    }
    @Override
    public ProgressLessonResponse getProgressLessons(long userId, int lessonId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
        ProgressLesson progressLesson = progressLessonRepository.findByUserAndLesson(user, lesson);
        return progressLessonMapper.toProgressLessonMapper(progressLesson);
    }
}
