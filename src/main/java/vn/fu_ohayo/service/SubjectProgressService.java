package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;

public interface SubjectProgressService {

    Page<ProgressSubjectResponse> getSubjectProgressBySubjectIdAndUserId(int subjectId, long userId, int page, int size);
    ProgressSubjectResponse getSubjectProgressBySubjectId(int subjectId);
}
