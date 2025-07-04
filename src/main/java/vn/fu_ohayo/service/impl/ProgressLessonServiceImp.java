package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressLessonResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.ProgressLesson;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressLessonMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.ProgressLessonRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressLessonService;

import java.util.Date;

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
    public ProgressLessonResponse getProgressLessons(String email, int lessonId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
        ProgressLesson progressLesson = progressLessonRepository.findByUserAndLesson(user, lesson);
        return progressLessonMapper.toProgressLessonMapper(progressLesson);
    }

    @Override
    public ProgressLessonResponse createProgressLesson(String username, int lessonId) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
        ProgressLesson existedProgressLesson = progressLessonRepository.findByUserAndLesson(user, lesson);
        if (existedProgressLesson != null) {
            existedProgressLesson.setViewedAt(new Date());
//            if (existedProgressLesson.getStatus() == ProgressStatus.IN_PROGRESS) {
//                existedProgressLesson.setStatus(ProgressStatus.COMPLETED);
//            }
            progressLessonRepository.save(existedProgressLesson);
            return progressLessonMapper.toProgressLessonMapper(existedProgressLesson);
        }
        ProgressLesson progressLesson =ProgressLesson.builder()
                .lesson(lesson)
                .status(ProgressStatus.IN_PROGRESS)
                .user(user)
                .build();
        return progressLessonMapper.toProgressLessonMapper(
                progressLessonRepository.save(progressLesson)
        );
    }

    @Override
    public ProgressLessonResponse updateProgressLesson(String username, int lessonId, boolean isCompleted) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
        ProgressLesson existedProgressLesson = progressLessonRepository.findByUserAndLesson(user, lesson);
        if (existedProgressLesson != null) {
            existedProgressLesson.setViewedAt(new Date());
            if (isCompleted) {
                existedProgressLesson.setStatus(ProgressStatus.COMPLETED);
                existedProgressLesson.setEndDate(new Date());
            } else {
                existedProgressLesson.setStatus(ProgressStatus.IN_PROGRESS);
            }
            return progressLessonMapper.toProgressLessonMapper(
                    progressLessonRepository.save(existedProgressLesson)
            );
        }
        return null;
    }
}
