package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExerciseQuestionRequest {
    private Integer questionId;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String questionText;

    @Size(min = 2, message = "The list must contain at least 2 answer")
    private List<AnswerQuestionRequest> answers;
}
