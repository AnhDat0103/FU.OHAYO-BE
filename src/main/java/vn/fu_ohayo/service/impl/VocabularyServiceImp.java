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
import java.util.stream.Collectors;

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
        List<Vocabulary> vocabularies = lesson.getVocabularies();
        return vocabularies.stream().map(vocabularyMapper::toVocabularyResponse).collect(Collectors.toList());
    }

    @Override
    public VocabularyResponse handleSaveVocabulary(int lessonId,VocabularyRequest vocabularyRequest) {
        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );

        if(vocabularyRepository.existsByKanjiAndKanaAndMeaningAndLessonId(vocabularyRequest.getKanji(),
                vocabularyRequest.getKana(),
                vocabularyRequest.getMeaning(), lessonId
           )){
            throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
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
                .build();
        vocabularyRepository.save(vocabulary);
        return vocabularyMapper.toVocabularyResponse(vocabulary);
    }

    @Override
    public VocabularyResponse updatePutVocabulary(int vocabularyId, VocabularyRequest vocabularyRequest) {
        Lesson lesson = lessonRepository.getLessonByLessonId(vocabularyRequest.getLessonId()).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );

        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId).orElseThrow(
                () -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND)
        );
        if(vocabularyRepository.existsDuplicateVocabularyInLessonExceptId(vocabularyRequest.getKanji(),
                    vocabularyRequest.getKana(),
                    vocabularyRequest.getMeaning(),
                    lesson.getLessonId(), vocabularyId)){
                throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
        }
        if(vocabularyRequest.getKanji() != null) {
            vocabulary.setKanji(vocabularyRequest.getKanji());
        }
        if(vocabularyRequest.getKana() != null) {
            vocabulary.setKana(vocabularyRequest.getKana());
        }
        if(vocabularyRequest.getRomaji() != null) {
            vocabulary.setRomaji(vocabularyRequest.getRomaji());
        }
        if(vocabularyRequest.getMeaning() != null) {
            vocabulary.setMeaning(vocabularyRequest.getMeaning());
        }
        if(vocabularyRequest.getDescription() != null) {
            vocabulary.setDescription(vocabularyRequest.getDescription());
        }
        if(vocabularyRequest.getExample() != null) {
            vocabulary.setExample(vocabularyRequest.getExample());
        }
        if(vocabularyRequest.getPartOfSpeech() != null) {
            vocabulary.setPartOfSpeech(vocabularyRequest.getPartOfSpeech());
        }
        if(vocabularyRequest.getJlptLevel() != null) {
            vocabulary.setJlptLevel(vocabularyRequest.getJlptLevel());
        }
        return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(vocabulary));
    }

    @Override
    public void deleteVocabularyById(int vocabularyId) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId).orElseThrow(
                () -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND)
        );
        vocabularyRepository.delete(vocabulary);

    }

    @Override
    public Page<VocabularyResponse> getVocabularyPage(int page, int size, int lessonId) {

        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        return vocabularyRepository.findAllByLessonId( lesson.getLessonId(), PageRequest.of(page, size))
                .map(vocabularyMapper::toVocabularyResponse);
    }

    @Override
    public Page<VocabularyResponse> getAllVocabulariesPage(int page, int size) {
        return vocabularyRepository.findAll(PageRequest.of(page, size))
                .map(vocabularyMapper::toVocabularyResponse);
    }

    @Override
    public VocabularyResponse handleSaveVocabulary(VocabularyRequest vocabularyRequest) {
        Vocabulary existingVocabulary = vocabularyRepository.findAllByKanjiAndKanaAndMeaning(
                vocabularyRequest.getKanji(),
                vocabularyRequest.getKana(),
                vocabularyRequest.getMeaning()
        );
        if(existingVocabulary != null) {
            if(existingVocabulary.getDeleted() == false) {
                throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
            } else{
                existingVocabulary.setDeleted(false);
                return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(existingVocabulary));
            }
        }
        Vocabulary vocabulary = vocabularyMapper.toVocabulary(vocabularyRequest);
        return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(vocabulary));
    }
}
