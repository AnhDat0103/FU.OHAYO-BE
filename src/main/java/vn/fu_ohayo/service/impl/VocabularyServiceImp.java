package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.VocabularyService;

import java.util.List;

@Service
public class VocabularyServiceImp implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyMapper vocabularyMapper;
    private final LessonRepository lessonRepository;

    public VocabularyServiceImp(VocabularyRepository vocabularyRepository, VocabularyMapper vocabularyMapper, LessonRepository lessonRepository) {
        this.vocabularyRepository = vocabularyRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<VocabularyResponse> getAllVocabularies(int lessonId) {
        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        return vocabularyRepository.findAllByLesson(lesson)
                .stream().map(vocabularyMapper::toVocabularyResponse).toList();
    }

    @Override
    public VocabularyResponse getVocabularyById(String id) {
        return null;
    }

    @Override
    public VocabularyResponse handleSaveVocabulary(int lessonId, VocabularyRequest vocabularyRequest) {
        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );

        if(vocabularyRepository.existsByKanjiAndLesson(vocabularyRequest.getKanji(), lesson)){
            throw new AppException(ErrorEnum.VOCABULARY_KANJI_EXISTS);
        }
        Vocabulary vocabulary = Vocabulary.builder()
                .kana(vocabularyRequest.getKana())
                .kanji(vocabularyRequest.getKanji())
                .meaning(vocabularyRequest.getMeaning())
                .romaji(vocabularyRequest.getRomaji())
                .partOfSpeech(vocabularyRequest.getPartOfSpeech())
                .jlptLevel(vocabularyRequest.getJlptLevel())
                .example(vocabularyRequest.getExample())
                .description(vocabularyRequest.getDescription())
                .lesson(lesson)
                .build();
        vocabularyRepository.save(vocabulary);
        return vocabularyMapper.toVocabularyResponse(vocabulary);
    }

    @Override
    public VocabularyResponse updatePutVocabulary(int lessonId, VocabularyRequest vocabularyRequest) {
        return null;
    }

    @Override
    public VocabularyResponse updatePatchVocabulary(String id, VocabularyRequest vocabularyRequest) {
        return null;
    }

    @Override
    public void deleteVocabularyById(String id) {

    }

    @Override
    public Page<VocabularyResponse> getVocabularyPage(int page, int size, int lessonId) {

        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        return vocabularyRepository.findAllByLesson(PageRequest.of(page, size), lesson)
                .map(vocabularyMapper::toVocabularyResponse);
    }
}
