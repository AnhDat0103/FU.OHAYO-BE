package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.SubjectStatus;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectResponse {

    private int subjectId;

    private String subjectName;

    private String subjectCode;

    private String description;

    private SubjectStatus status;

    private Date updatedAt;
}
