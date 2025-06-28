package vn.fu_ohayo.dto.response;

import lombok.Data;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

import java.util.Date;
@Data
public class QuizResponse {

    private String vocabularyId;

    private String kanji;

    private String kana;

    private String romaji;

    private String meaning;

    private String description;

    private String example;

    private PartOfSpeech partOfSpeech;

    private String quizQuestion;
}
