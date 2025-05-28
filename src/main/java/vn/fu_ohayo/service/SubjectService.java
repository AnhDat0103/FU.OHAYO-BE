package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {

     List<SubjectResponse> getAllSubjects();
     SubjectResponse createSubject(SubjectRequest subjectRequest);
     SubjectResponse updateSubject(int id, SubjectRequest subjectRequest);
     void deleteSubject(int id);
}
