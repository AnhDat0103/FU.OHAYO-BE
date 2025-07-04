package vn.fu_ohayo.dto.response;

import vn.fu_ohayo.dto.request.AnswerQuestionRequest;

import java.util.Date;
import java.util.List;

public class ExerciseQuestion2Response {
    private int exerciseQuestionId;
    private String questionText;
    private Date createdAt;
    private Date updatedAt;
    private List<AnswerQuestionResponse> answerQuestions;
}
