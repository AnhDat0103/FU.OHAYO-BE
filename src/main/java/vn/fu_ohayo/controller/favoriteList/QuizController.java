package vn.fu_ohayo.controller.favoriteList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.VocabularyResponse;
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
    private final QuizService quizDataService;

    public QuizController(FavoriteVocabularyRepository favoriteVocabularyRepository, VocabularyMapper vocabularyMapper, QuizService quizDataService) {
        this.favoriteVocabularyRepository = favoriteVocabularyRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.quizDataService = quizDataService;
    }

    @GetMapping()
    private ResponseEntity<Set<VocabularyResponse>> getList(@RequestParam("id") Integer id) {
        quizDataService.getQuestion(id);
        Set<VocabularyResponse> list = favoriteVocabularyRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).
                getVocabularies()
                .stream()
                .map(vocabularyMapper :: toVocabularyResponse).collect(Collectors.toSet());
        return ResponseEntity.ok(list);
    }

}
