package vn.fu_ohayo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerQuestionRequest {
    private String answerText;
    @JsonProperty("correct")
    private Boolean isCorrect;

}
