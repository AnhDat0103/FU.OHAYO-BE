package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressSubjectResponse;

public interface SubjectProgressService {

    ProgressSubjectResponse getSubjectProgressBySubjectIdAndUserId(int subjectId, long userId);
    ProgressSubjectResponse getSubjectProgressBySubjectId(int subjectId);
}
