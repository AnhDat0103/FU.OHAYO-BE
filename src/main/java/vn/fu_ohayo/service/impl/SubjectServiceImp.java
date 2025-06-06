package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final LessonRepository lessonRepository;

    public SubjectServiceImp(SubjectRepository subjectRepository, SubjectMapper subjectMapper, LessonRepository lessonRepository) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Page<SubjectResponse> getAllSubjects(int page, int size) {
        return subjectRepository.findAllByStatus(SubjectStatus.ACTIVE, PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(subjectRepository.countUsersBySubjectId(s.getSubjectId()) > 0 ? subjectRepository.countUsersBySubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    return s;
                });
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        if (subjectRepository.existsBySubjectCode(subjectRequest.getSubjectCode())) {
            throw new AppException(ErrorEnum.SUBJECT_CODE_EXISTS);
        }
        if(subjectRepository.existsBySubjectName(subjectRequest.getSubjectName())) {
            throw new AppException(ErrorEnum.SUBJECT_NAME_EXISTS);
        }
        Subject subject = subjectMapper.toSubject(subjectRequest);
        return subjectMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse updateSubject(int id, SubjectRequest subjectRequest) throws AppException {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if(subjectRequest.getSubjectCode() != null){
            if (subjectRepository.existsBySubjectCodeAndSubjectIdNot(subjectRequest.getSubjectCode(), id)) {
                throw new AppException(ErrorEnum.SUBJECT_CODE_EXISTS);
            }
            subject.setSubjectCode(subjectRequest.getSubjectCode());
        }
        if(subjectRequest.getSubjectName() != null){
            if(subjectRepository.existsBySubjectNameAndSubjectIdNot(subjectRequest.getSubjectName(), id)) {
                throw new AppException(ErrorEnum.SUBJECT_NAME_EXISTS);
            }
            subject.setSubjectName(subjectRequest.getSubjectName());
        }
        if(subjectRequest.getDescription() != null){
            subject.setDescription(subjectRequest.getDescription());
        }
        if(subjectRequest.getStatus() != null){
            subject.setStatus(subjectRequest.getStatus());
        }
        Subject updatedSubject = subjectRepository.save(subject);
        return subjectMapper.toSubjectResponse(updatedSubject);
    }

    @Override
    public void deleteSubject(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if(subjectRepository.countUsersBySubjectId(id) > 0) {
            throw new AppException(ErrorEnum.SUBJECT_IN_USE);
        }
        subjectRepository.delete(subject);
    }

    @Override
    public SubjectResponse getSubjectById(int id) {
        return subjectRepository.findById(id)
                .map(subjectMapper::toSubjectResponse)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
    }

}
