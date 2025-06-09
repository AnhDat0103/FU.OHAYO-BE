package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressSubjectService;

import java.util.Date;

@Service
public class ProgressSubjectServiceImp implements ProgressSubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProgressSubjectRepository progressSubjectRepository;
    private final SubjectMapper subjectMapper;


    public ProgressSubjectServiceImp(SubjectRepository subjectRepository,
                                     UserRepository userRepository,
                                     UserMapper userMapper,
                                     ProgressSubjectRepository progressSubjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.progressSubjectRepository = progressSubjectRepository;
        this.subjectMapper = subjectMapper;
    }
    @Override
    public void enrollCourse(int courseId, long userId) {
        Subject subject = subjectRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        ProgressSubject existingProgress = progressSubjectRepository.findBySubjectAndUserAndProgressStatus(subject, user, ProgressStatus.IN_PROGRESS);
        if (existingProgress != null) {
            existingProgress.setStartDate(new Date());
            return;
        }

        ProgressSubject progressSubject = ProgressSubject.builder()
                .subject(subject)
                .startDate(new Date())
                .user(user)
                .progressStatus(ProgressStatus.IN_PROGRESS)
                .build();
        progressSubjectRepository.save(progressSubject);
    }


}
