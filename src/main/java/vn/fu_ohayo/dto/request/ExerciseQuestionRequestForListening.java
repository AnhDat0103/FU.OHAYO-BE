package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExerciseQuestionRequestForListening {
    @NotNull(message = "Content listening ID cannot be null")
    private long content_listening_id;
    @NotNull(message = "Question text cannot be null")
    @NotBlank(message = "Question text cannot be blank")
    private String questionText;
    @NotNull(message = "The list must contain at least 2 answer")
    @Size(min = 2, message = "The list must contain at least 2 answer")
    private List<AnswerQuestionRequest> answerQuestions;
}
