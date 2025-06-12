package vn.fu_ohayo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerQuestionRequest {
//    @NotNull(message = "Answer text cannot be null")
//    @NotBlank(message = "Answer text cannot be blank")
    private int answerId;
    private String answerText;
    @JsonProperty("correct")
    private Boolean isCorrect;

}
