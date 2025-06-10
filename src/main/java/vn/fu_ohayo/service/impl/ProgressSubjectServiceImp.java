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
import java.util.Optional;

@Service
public class ProgressSubjectServiceImp implements ProgressSubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;


    public ProgressSubjectServiceImp(SubjectRepository subjectRepository,
                                     UserRepository userRepository,
                                     ProgressSubjectRepository progressSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
    }
    @Override
    public void enrollCourse(int courseId, long userId) {
        Subject subject = subjectRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        ProgressSubject existingProgress = progressSubjectRepository.findProgressSubjectBySubjectAndUser(subject, user);

        if (existingProgress != null) {
                existingProgress.setViewedAt(new Date());
                return;
        }

        ProgressSubject progressSubject = ProgressSubject.builder()
                .subject(subject)
                .startDate(new Date())
                .viewedAt(new Date())
                .user(user)
                .progressStatus(ProgressStatus.IN_PROGRESS)
                .build();
        progressSubjectRepository.save(progressSubject);
    }


}
