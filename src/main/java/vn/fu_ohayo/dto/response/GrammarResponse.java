package vn.fu_ohayo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GrammarResponse {

    private int grammarId;

    private String titleJp;

    private String structure;

    private String meaning;

    private String usage;

    private String example;

    private JlptLevel jlptLevel;

    private int lessonId;

    private Date updateAt;
}
