package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.LessonMapper;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.LessonService;

import java.util.List;

@Service
public class LessonServiceImp implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final SubjectRepository subjectRepository;
    private final VocabularyRepository vocabularyRepository;
    private final GrammarRepository grammarRepository;

    public LessonServiceImp(LessonRepository lessonRepository, LessonMapper lessonMapper,
                            SubjectRepository subjectRepository,
                            VocabularyRepository vocabularyRepository,
                            GrammarRepository grammarRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.subjectRepository = subjectRepository;
        this.vocabularyRepository = vocabularyRepository;
        this.grammarRepository = grammarRepository;
    }

    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) {
        Subject subject = subjectRepository.findById(lessonRequest.getSubjectId()).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );
        if (lessonRepository.existsByName(lessonRequest.getName())) {
            throw new AppException(ErrorEnum.LESSON_NAME_EXIST);
        }
        Lesson lesson = Lesson.builder()
                .name(lessonRequest.getName())
                .description(lessonRequest.getDescription())
                .subject(subject)
                .status(lessonRequest.getStatus())
                .build();
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(lessonMapper::toLessonResponse)
                .toList();
    }

    @Override
    public Page<LessonResponse> getAllLessons(int subjectId, int page, int pageSize) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );
        Page<Lesson> lessons = lessonRepository.findAllBySubject(subject,PageRequest.of(page, pageSize));
        return lessons.map(lessonMapper::toLessonResponse);
    }

    @Override
    public LessonResponse getLessonById(int id) {
        return lessonMapper.toLessonResponse(lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        ));
    }

    @Override
    public LessonResponse updateLesson(Integer id, LessonPatchRequest lessonRequest) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if(lessonRequest.getName() != null) {
            if (lessonRepository.existsByName(lessonRequest.getName())) {
                throw new AppException(ErrorEnum.LESSON_NAME_EXIST);
            }
            lesson.setName(lessonRequest.getName());
        }
        if(lessonRequest.getDescription() != null) {
            lesson.setDescription(lessonRequest.getDescription());
        }
        if(lessonRequest.getStatus() != null) {
            lesson.setStatus(lessonRequest.getStatus());
        }
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public void deleteLesson(Integer id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if(vocabularyRepository.countAllByLesson(lesson) > 0) {
            throw new AppException(ErrorEnum.LESSON_HAS_VOCABULARY);
        }
        if(grammarRepository.countAllByLesson(lesson) > 0) {
            throw new AppException(ErrorEnum.LESSON_HAS_GRAMMAR);
        }
        lessonRepository.delete(lesson);
    }
}
