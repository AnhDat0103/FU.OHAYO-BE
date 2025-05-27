package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;



@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectServiceImp(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAllSubjectWithUserCount()
                .stream()
            .map(subject -> SubjectResponse.builder()
                    .subjectCode(subject.getSubjectCode())
                    .subjectName(subject.getSubjectName())
                    .subjectId(subject.getSubjectId())
                    .description(subject.getDescription())
                    .status(subject.getStatus())
                    .updatedAt(subject.getUpdatedAt())
                    .countUsers(subject.getCountUsers())
                    .build())
                .toList();
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
            if (subjectRepository.existsBySubjectCode(subjectRequest.getSubjectCode())) {
                throw new AppException(ErrorEnum.SUBJECT_CODE_EXISTS);
            }
            subject.setSubjectCode(subjectRequest.getSubjectCode());
        }
        if(subjectRequest.getSubjectName() != null){
            if(subjectRepository.existsBySubjectName(subjectRequest.getSubjectName())) {
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

}
