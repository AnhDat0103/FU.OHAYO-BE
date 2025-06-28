package vn.fu_ohayo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.fu_ohayo.entity.FavoriteVocabulary;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.repository.QuizRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service

public class QuizService {
    final QuizRepository quizRepository;
    final RestTemplate restTemplate;
    final FavoriteVocabularyRepository favoriteVocabularyRepository;

    public void getQuestion(Integer favoriteVocabularyId) {
        FavoriteVocabulary favoriteVocabulary = favoriteVocabularyRepository.findById(favoriteVocabularyId)
                .orElseThrow(() -> new RuntimeException("Favorite vocabulary not found"));

        favoriteVocabulary.getVocabularies().forEach(vocabulary -> {
            if (vocabulary.getQuizQuestion() == null) {
                quizRepository.save(QuizQuestion.builder()
                        .vocabulary(vocabulary)
                        .question(generateQuiz(vocabulary.getKanji() + " (" + vocabulary.getKana() + ")"))
                        .build());
            }
        });
    }

    public String generateQuiz(String word) {
        String API_KEY = "AIzaSyBUILxmrrbIGNiCcLZaN6RTYom3L9mW0F0";
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String text = "I will give you a vocabulary word. Please generate one multiple-choice question where the word I give is the correct answer, but do not include that word in the question itself. Only return the question sentence, no answer options or explanations. Vocabulary word" + word;
        Map<String, Object> part = Map.of("text", text);
        Map<String, Object> message = Map.of("parts", List.of(part));

        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7,
                "topK", 32,
                "topP", 1,
                "maxOutputTokens", 1024
        );

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(message));
        body.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            String resultText = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

            return resultText;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
