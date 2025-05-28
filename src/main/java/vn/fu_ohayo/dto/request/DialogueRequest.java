package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DialogueRequest {
    private String questionJp;

    private String questionVn;

    private String answerVn;

    private String answerJp;

    private long contentSpeakingId;
}
