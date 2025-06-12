package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProgressSubjectRepository progressSubjectRepository;

    public SubjectServiceImp(SubjectRepository subjectRepository, SubjectMapper subjectMapper,
                             LessonRepository lessonRepository,
                             UserRepository userRepository,
                             UserMapper userMapper,
                             ProgressSubjectRepository progressSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.progressSubjectRepository = progressSubjectRepository;
    }

    @Override
    public Page<SubjectResponse> getAllActiveSubjects(int page, int size, long userId) {
        return subjectRepository.findAllByStatusAndProgressSubjectsIsEmpty(SubjectStatus.ACTIVE,userId, PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) > 0 ? progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    return s;
                });
    }


    @Override
    public Page<SubjectResponse> getAllSubjectsForAdmin(int page, int size) {
        return subjectRepository.findAll(PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) > 0 ? progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectId(s.getSubjectId()), 0));
                    return s;
                });
    }

    @Override
    public Page<ProgressSubjectResponse> getAllByUserId(int page, int size, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Page<ProgressSubject> progressSubjects = progressSubjectRepository.findAllByUserAndSubject_Status(user, SubjectStatus.ACTIVE, PageRequest.of(page, size));

        if (progressSubjects.hasContent()) {
            return progressSubjects.map(ps -> {
                ProgressSubjectResponse response = new ProgressSubjectResponse();
                response.setProgressId(ps.getProgressId());
                response.setUser(userMapper.toUserResponse(ps.getUser()));
                response.setSubject(subjectMapper.toSubjectResponse(ps.getSubject()));
                response.setProgressStatus(ps.getProgressStatus());
                response.getSubject().setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(ps.getSubject().getSubjectId(), LessonStatus.PUBLIC), 0));
                response.getSubject().setCountUsers(
                        Math.max(progressSubjectRepository.countUserBySubject_SubjectId(ps.getSubject().getSubjectId()), 0));
                return response;
            });
        }
        return Page.empty();
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
        if(progressSubjectRepository.countUserBySubject_SubjectId(id) > 0) {
            throw new AppException(ErrorEnum.SUBJECT_IN_USE);
        }
        subjectRepository.delete(subject);
    }

    @Override
    public SubjectResponse getSubjectById(int id) {
        return subjectRepository.findById(id)
                .map(s -> {
                    SubjectResponse response = subjectMapper.toSubjectResponse(s);
                    response.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    response.setCountUsers(Math.max(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()), 0));
                    return response;
                })
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
    }

}
