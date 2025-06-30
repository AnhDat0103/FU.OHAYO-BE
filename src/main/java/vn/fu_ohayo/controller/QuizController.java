package vn.fu_ohayo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.QuizVocabularyResponse;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.service.QuizService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")

public class QuizController {

    private final FavoriteVocabularyRepository favoriteVocabularyRepository;
    private final VocabularyMapper vocabularyMapper;
    private final QuizService quizService;
    public QuizController(FavoriteVocabularyRepository favoriteVocabularyRepository, VocabularyMapper vocabularyMapper, QuizService quizService) {
        this.favoriteVocabularyRepository = favoriteVocabularyRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.quizService = quizService;
    }

    @GetMapping("/vocabulary-list")
    public ResponseEntity<Set<QuizVocabularyResponse>> getAllVocabularyList(@RequestParam("id") int id) {
        quizService.genVocabQuestion(id);
        Set<QuizVocabularyResponse> list = favoriteVocabularyRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).
                getVocabularies()
                .stream()
                .map(vocabularyMapper :: toQuizResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/grammar-list")
    public ResponseEntity<Set<QuizVocabularyResponse>> getAllGrammarList(@RequestParam("id") int id) {
        quizService.genVocabQuestion(id);
        Set<QuizVocabularyResponse> list = favoriteVocabularyRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).
                getVocabularies()
                .stream()
                .map(vocabularyMapper :: toQuizResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(list);
    }

}
