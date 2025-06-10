package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.service.SubjectProgressService;


@Service
public class SubjectProgressServiceImp implements SubjectProgressService {

    private final ProgressSubjectRepository progressSubjectRepository;

    public SubjectProgressServiceImp(ProgressSubjectRepository progressSubjectRepository) {
        this.progressSubjectRepository = progressSubjectRepository;
    }

    @Override
    public Page<ProgressSubjectResponse> getSubjectProgressBySubjectIdAndUserId(int subjectId, long userId, int page, int size) {

        return null;
    }

    @Override
    public ProgressSubjectResponse getSubjectProgressBySubjectId(int subjectId) {
        return null;
    }
}
