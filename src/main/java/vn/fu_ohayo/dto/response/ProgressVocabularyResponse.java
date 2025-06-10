package vn.fu_ohayo.dto.response;

import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

public class ProgressVocabularyResponse {
    private int progressId;
    private VocabularyResponse vocabulary;
    private ProgressStatus progressStatus;
    private Date reviewedAt;


}
