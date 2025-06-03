package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.service.GrammarService;

import java.util.List;
import java.util.Optional;

@Service
public class GrammarServiceImp implements GrammarService {

    private final ContentServiceImp contentServiceImp;
    private final LessonRepository lessonRepository;
    private final GrammarRepository grammarRepository;
    private final GrammarMapper grammarMapper;
    public GrammarServiceImp(ContentServiceImp contentServiceImp,
                             LessonRepository lessonRepository,
                             GrammarRepository grammarRepository,
                             GrammarMapper grammarMapper) {
        this.contentServiceImp = contentServiceImp;
        this.lessonRepository = lessonRepository;
        this.grammarRepository = grammarRepository;
        this.grammarMapper = grammarMapper;
    }
    @Override
    public List<GrammarResponse> getAllGrammars() {
        return null;
    }

    @Override
    public GrammarResponse getGrammarById(String id) {
        return null;
    }

    @Override
    public GrammarResponse saveGrammar(GrammarRequest grammarRequest) {
        Lesson lesson = lessonRepository.findById(grammarRequest.getLessonId()).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        Grammar grammar = grammarRepository.findGrammarByTitleJpAndLesson(grammarRequest.getTitleJp(), lesson).orElseThrow(
                () -> new AppException(ErrorEnum.GRAMMAR_EXISTED)
        );
        Grammar newGrammar = Grammar.builder()
                .titleJp(grammarRequest.getTitleJp())
                .jlptLevel(grammarRequest.getJlptLevel())
                .example(grammarRequest.getExample())
                .meaning(grammarRequest.getMeaning())
                .structure(grammarRequest.getStructure())
                .usage(grammarRequest.getUsage())
                .lesson(lesson)
                .usage(grammarRequest.getUsage())
                .build();
        return grammarMapper.toGrammarResponse(grammarRepository.save(newGrammar));
    }

    @Override
    public GrammarResponse updateGrammar(String id, GrammarRequest grammarRequest) {
        return null;
    }

    @Override
    public GrammarResponse patchGrammar(String id, GrammarRequest grammarRequest) {
        return null;
    }

    @Override
    public void deleteGrammarById(String id) {

    }

    @Override
    public Page<GrammarResponse> getGrammarPage(int page, int size) {
        return null;
    }

    @Override
    public Page<GrammarResponse> getAllGrammars(int lessonId, int page, int size) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));

        Page<Grammar> grammarPage = grammarRepository.findAllByLesson(lesson, PageRequest.of(page, size));
        return grammarPage.map(grammarMapper::toGrammarResponse);
    }
}
