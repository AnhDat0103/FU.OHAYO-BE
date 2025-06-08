package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {

     Page<SubjectResponse> getAllSubjects(int page, int size);
     SubjectResponse createSubject(SubjectRequest subjectRequest);
     SubjectResponse updateSubject(int id, SubjectRequest subjectRequest);
     void deleteSubject(int id);
    SubjectResponse getSubjectById(int id);
    Page<SubjectResponse> getAllSubjectsForAdmin(int page, int size);

    Page<SubjectResponse> getAllByUserId(int page, int size, long userId);
}
