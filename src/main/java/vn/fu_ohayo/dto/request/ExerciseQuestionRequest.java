package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.AnswerQuestion;

import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExerciseQuestionRequest {
    private long content_listening_id;
    private String questionText;
    private List<AnswerQuestionRequest> answerQuestionRequests;
}
