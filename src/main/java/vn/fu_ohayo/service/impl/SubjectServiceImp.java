package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;



@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImp(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(subject -> SubjectResponse.builder()
                        .subjectCode(subject.getSubjectCode())
                        .subjectName(subject.getSubjectName())
                        .description(subject.getDescription())
                        .countOfLessons(subject.getLessons().size())
                        .countOfStudents(subject.getUsers().size())
                        .build())
                .toList();
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        return null;
    }

    @Override
    public SubjectResponse updateSubject(int id, SubjectRequest subjectRequest) {
        return null;
    }

    @Override
    public void deleteSubject(int id) {

    }
}
