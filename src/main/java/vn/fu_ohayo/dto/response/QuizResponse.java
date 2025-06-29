package vn.fu_ohayo.dto.response;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;
import vn.fu_ohayo.enums.PartOfSpeech;
@Getter
@Setter
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
