package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.service.SubjectProgressService;


@Service
public class SubjectProgressServiceImp implements SubjectProgressService {

    @Override
    public ProgressSubjectResponse getSubjectProgressBySubjectIdAndUserId(int subjectId, long userId) {
        return null;
    }

    @Override
    public ProgressSubjectResponse getSubjectProgressBySubjectId(int subjectId) {
        return null;
    }
}
